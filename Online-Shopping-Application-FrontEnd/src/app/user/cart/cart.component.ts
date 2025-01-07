import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class UserCartComponent implements OnInit {
  cart: { [productId: number]: number } = {}; // productId -> quantity
  products: any[] = [];
  errorMsg: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadCart();
    this.fetchCartProducts();
  }

  loadCart(): void {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart = JSON.parse(storedCart);
    }
  }

  saveCart(): void {
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }

  fetchCartProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const productIds = Object.keys(this.cart);

    if (productIds.length === 0) {
      this.errorMsg = 'Your shopping cart is empty!';
      this.products = [];
      return;
    }

    Promise.all(
      productIds.map((id) =>
        this.http
          .get<any>(`http://localhost:8080/products/${id}`, { headers })
          .toPromise()
      )
    )
      .then((responses) => {
        this.products = responses.map((res) => res.data);
        this.errorMsg = '';
      })
      .catch((error) => {
        this.errorMsg = 'Failed to fetch product details.';
      });
  }

  addOne(productId: number): void {
    if (this.cart[productId]) {
      this.cart[productId]++;
    } else {
      this.cart[productId] = 1;
    }
    this.saveCart();
    this.fetchCartProducts();
  }

  deleteOne(productId: number): void {
    if (this.cart[productId]) {
      this.cart[productId]--;
      if (this.cart[productId] === 0) {
        delete this.cart[productId];
      }
      this.saveCart();
      this.checkCartState();
    }
  }

  deleteAll(productId: number): void {
    delete this.cart[productId];
    this.saveCart();
    this.checkCartState();
  }

  checkCartState(): void {
    if (Object.keys(this.cart).length === 0) {
      this.errorMsg = 'Your shopping cart is empty!';
      this.products = [];
      return;
    }
    this.fetchCartProducts();
  }

  placeOrder(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const order = Object.entries(this.cart).map(([productId, quantity]) => ({
      productId: +productId,
      quantity,
    }));

    this.http
      .post<any>('http://localhost:8080/orders', { order }, { headers })
      .subscribe(
        (response) => {
          alert(response.message);
          if (response.success) {
            this.cart = {};
            this.saveCart();
            this.products = [];
            this.errorMsg = 'Your shopping cart is empty!';
          }
        },
        (error) => {
          if (error.status === 400 && error.error?.message) {
            const match = error.error.message.match(/product ID: (\d+)/);
            if (match) {
              const productId = +match[1];

              // Fetch the product details to get the product name
              this.http
                .get<any>(`http://localhost:8080/products/${productId}`, {
                  headers,
                })
                .subscribe(
                  (productResponse) => {
                    const productName = productResponse.data?.name;
                    this.errorMsg = `Insufficient stock for product: ${
                      productName || `ID: ${productId}`
                    }`;
                    alert(this.errorMsg);
                  },
                  (productError) => {
                    this.errorMsg = `Insufficient stock for product ID: ${productId}`;
                    alert(this.errorMsg);
                  }
                );
            } else {
              this.errorMsg = error.error.message;
              alert(this.errorMsg);
            }
          } else {
            alert('Failed to place order due to an unexpected error.');
          }
        }
      );
  }
}
