import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css'],
})
export class AdminOrderComponent implements OnInit {
  orders: any[] = [];
  filteredOrders: any[] = [];
  filterStatus: string = 'all'; // default
  errorMsg: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchOrders();
  }

  fetchOrders(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>('http://localhost:8080/orders/all', { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.orders = response.data;
            this.applyFilter();
          } else {
            this.errorMsg = response.message;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch orders.';
        }
      );
  }

  applyFilter(): void {
    if (this.filterStatus === 'all') {
      this.filteredOrders = this.orders;
    } else {
      this.filteredOrders = this.orders.filter(
        (order) => order.orderStatus.toLowerCase() === this.filterStatus
      );
    }
  }

  changeFilter(status: string): void {
    this.filterStatus = status;
    this.applyFilter();
  }

  viewOrder(orderId: number): void {
    this.router.navigate([`/admin/orders/${orderId}`]);
  }

  completeOrder(orderId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .patch<any>(
        `http://localhost:8080/orders/${orderId}/complete`,
        {},
        { headers }
      )
      .subscribe(
        (response) => {
          alert(response.message);
          if (response.success) {
            this.fetchOrders(); // refresh the page
          }
        },
        (error) => {
          alert('Failed to complete order.');
        }
      );
  }

  cancelOrder(orderId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .patch<any>(
        `http://localhost:8080/orders/${orderId}/cancel`,
        {},
        { headers }
      )
      .subscribe(
        (response) => {
          alert(response.message);
          if (response.success) {
            this.fetchOrders(); // refresh
          }
        },
        (error) => {
          alert('Failed to cancel order.');
        }
      );
  }
}
