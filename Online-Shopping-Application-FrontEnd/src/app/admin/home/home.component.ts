import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class AdminHomeComponent {
  showProfitProducts: boolean = false;
  showPopularProducts: boolean = false;
  showTotalSoldProducts: boolean = false;

  profitProducts: any[] = [];
  popularProducts: any[] = [];
  totalSoldProducts: any[] = [];
  errorMsg: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  logout(): void {
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/login']);
  }

  toggleProfitProducts(): void {
    if (this.showProfitProducts) {
      this.showProfitProducts = false;
    } else {
      this.fetchProfitProducts();
    }
  }

  togglePopularProducts(): void {
    if (this.showPopularProducts) {
      this.showPopularProducts = false;
    } else {
      this.fetchPopularProducts();
    }
  }

  toggleTotalSoldProducts(): void {
    if (this.showTotalSoldProducts) {
      this.showTotalSoldProducts = false;
    } else {
      this.fetchTotalSoldProducts();
    }
  }

  fetchProfitProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/products/profit/3', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.profitProducts = response.data;
            this.showProfitProducts = true;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch profitable products.';
        }
      );
  }

  fetchPopularProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/products/popular/3', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.popularProducts = response.data;
            this.showPopularProducts = true;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch popular products.';
        }
      );
  }

  fetchTotalSoldProducts(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/products/total-amount-sold', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.totalSoldProducts = response.data.sort(
              (a: any, b: any) => b.totalSoldAmount - a.totalSoldAmount
            );
            this.showTotalSoldProducts = true;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch total sold products.';
        }
      );
  }

  navigateToProduct(productId: number): void {
    this.router.navigate([`/admin/product/${productId}`]);
  }
}
