import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-admin-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.css'],
})
export class AdminOrderDetailComponent implements OnInit {
  order: any = null;
  errorMsg: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const orderId = this.route.snapshot.params['orderId'];
    this.fetchOrderDetails(orderId);
  }

  fetchOrderDetails(orderId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>(`http://localhost:8080/orders/${orderId}`, { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.order = response.data;
            this.errorMsg = '';
          } else {
            this.errorMsg = response.message;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch order details.';
        }
      );
  }
}
