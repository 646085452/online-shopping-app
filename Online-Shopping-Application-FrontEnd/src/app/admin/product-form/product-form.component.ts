import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css'],
})
export class AdminProductFormComponent {
  productForm: FormGroup;
  errorMsg: string = '';
  successMsg: string = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      wholesalePrice: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d+(\.\d{1,2})?$/),
          Validators.min(0),
        ],
      ],
      retailPrice: [
        '',
        [
          Validators.required,
          Validators.pattern(/^\d+(\.\d{1,2})?$/),
          Validators.min(0),
        ],
      ],
      quantity: [
        '',
        [Validators.required, Validators.pattern(/^\d+$/), Validators.min(0)],
      ],
    });
  }

  saveProduct(): void {
    if (this.productForm.invalid) {
      this.errorMsg = 'Please correct the errors in the form.';
      return;
    }

    const productData = this.productForm.value;
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .post<any>('http://localhost:8080/products', productData, { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.successMsg = 'Product created successfully.';
            this.errorMsg = '';
            this.router.navigate(['/admin/product']);
          } else {
            this.errorMsg = response.message;
            this.successMsg = '';
          }
        },
        (error) => {
          this.errorMsg = 'Failed to create product.';
          this.successMsg = '';
        }
      );
  }

  disregardChanges(): void {
    this.router.navigate(['/admin/product']); // Redirect to admin/product
  }
}
