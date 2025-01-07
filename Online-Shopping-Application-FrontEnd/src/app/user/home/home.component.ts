import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class UserHomeComponent {
  showFrequent: boolean = false;
  showRecent: boolean = false;
  frequentProducts: any[] = [];
  recentProducts: any[] = [];
  errorMsg: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  logout(): void {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('cart');
    this.router.navigate(['/login']);
  }

  toggleFrequentProducts(): void {
    if (this.showFrequent) {
      this.showFrequent = false;
    } else {
      this.fetchFrequentProducts();
    }
  }

  toggleRecentProducts(): void {
    if (this.showRecent) {
      this.showRecent = false;
    } else {
      this.fetchRecentProducts();
    }
  }

  fetchFrequentProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/products/frequent/3', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.frequentProducts = response.data;
            this.showFrequent = true;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch top frequent products.';
        }
      );
  }

  fetchRecentProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/products/recent/3', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.recentProducts = response.data;
            this.showRecent = true;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch top recent products.';
        }
      );
  }

  navigateToProduct(productId: number): void {
    this.router.navigate([`/user/product/${productId}`]);
  }
}
