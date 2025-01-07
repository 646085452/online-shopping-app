import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.css'],
})
export class UserOrderDetailComponent implements OnInit {
  order: any;
  errorMsg: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

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
          } else {
            this.router.navigate(['/unauthorized']);
          }
        },
        (error) => {
          this.errorMsg = error.error?.message;
        }
      );
  }

  viewProduct(productId: number): void {
    this.router.navigate([`/user/product/${productId}`]);
  }
}
