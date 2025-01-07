import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-admin-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
})
export class AdminProductDetailComponent implements OnInit {
  product: any = null;
  errorMsg: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.params['productId'];
    this.fetchProductDetails(productId);
  }

  fetchProductDetails(productId: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>(`http://localhost:8080/products/${productId}`, { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.product = response.data;
            this.errorMsg = '';
          } else {
            this.errorMsg = response.message;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch product details.';
        }
      );
  }
}
