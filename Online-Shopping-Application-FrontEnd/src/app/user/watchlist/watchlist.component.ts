import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css'],
})
export class UserWatchlistComponent implements OnInit {
  watchlist: any[] = [];
  errorMsg: string = '';
  cart: { [productId: number]: number } = {}; // productId -> quantity

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadCart();
    this.fetchWatchlist();
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

  fetchWatchlist(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/watchlist/products/all', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.watchlist = response.data;
            this.errorMsg = ''; // Clear error message
          } else {
            this.errorMsg = response.message; // Set error message if no products
            this.watchlist = [];
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch watchlist items.';
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

  removeFromWatchlist(productId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .delete<any>(`http://localhost:8080/watchlist/product/${productId}`, {
        headers,
      })
      .subscribe(
        (response) => {
          alert(response.message); // Pop up the response message
          this.fetchWatchlist(); // Reload the watchlist
        },
        (error) => {
          alert('Failed to remove product from watchlist.');
        }
      );
  }
}
