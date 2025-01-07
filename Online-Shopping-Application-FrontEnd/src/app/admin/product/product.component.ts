import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class AdminProductComponent implements OnInit {
  products: any[] = [];
  errorMsg: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchProducts();
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
            this.errorMsg = '';
          } else {
            this.errorMsg = response.message;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch products.';
        }
      );
  }

  viewProduct(productId: number): void {
    this.router.navigate([`/admin/product/${productId}`]);
  }

  editProduct(productId: number): void {
    this.router.navigate([`/admin/product-edit/${productId}`]);
  }

  addNewProduct(): void {
    this.router.navigate(['/admin/product-form']);
  }
}
