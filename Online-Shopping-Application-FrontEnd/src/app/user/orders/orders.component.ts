import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css'],
})
export class UserOrdersComponent implements OnInit {
  orders: any[] = [];
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
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch orders.';
        }
      );
  }

  viewOrder(orderId: number): void {
    this.router.navigate([`/user/orders/${orderId}`]);
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
          if (response.success) {
            alert('Order canceled successfully.');
            this.fetchOrders(); // reload after cancellation
          } else {
            alert(response.message);
          }
        },
        (error) => {
          alert('Failed to cancel order.');
        }
      );
  }
}
