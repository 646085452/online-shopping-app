import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class UserProductComponent implements OnInit {
  products: any[] = [];
  errorMsg: string = '';
  cart: { [productId: number]: number } = {}; // productId -> quantity

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchProducts();
    this.loadCart();
  }

  fetchProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/products/all', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.products = response.data;
          } else {
            this.errorMsg = 'Failed to fetch products.';
          }
        },
        (error) => {
          this.errorMsg = 'Error occurred while fetching products.';
        }
      );
  }

  addToCart(productId: number): void {
    if (this.cart[productId]) {
      this.cart[productId]++;
    } else {
      this.cart[productId] = 1;
    }
    this.saveCart();
    alert('Product added to cart successfully!');
  }

  addToWatchlist(productId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .post<any>(
        `http://localhost:8080/watchlist/product/${productId}`,
        {},
        { headers }
      )
      .subscribe(
        (response) => {
          alert(response.message); // Pop up message for success or failure
        },
        (error) => {
          alert('Failed to add product to watchlist.');
        }
      );
  }

  saveCart(): void {
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }

  loadCart(): void {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart = JSON.parse(storedCart);
    }
  }
}
