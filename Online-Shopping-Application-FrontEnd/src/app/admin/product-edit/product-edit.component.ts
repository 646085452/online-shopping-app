import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css'],
})
export class AdminProductEditComponent implements OnInit {
  productForm: FormGroup;
  originalProduct: any = null;
  productId: number;
  errorMsg: string = '';
  successMsg: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private fb: FormBuilder
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

    this.productId = this.route.snapshot.params['productId'];
  }

  ngOnInit(): void {
    this.fetchProductDetails();
  }

  fetchProductDetails(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .get<any>(`http://localhost:8080/products/${this.productId}`, { headers })
      .subscribe(
        (response) => {
          if (response.success) {
            this.originalProduct = response.data;
            this.populateForm();
          } else {
            this.errorMsg = response.message;
          }
        },
        (error) => {
          this.errorMsg = 'Failed to fetch product details.';
        }
      );
  }

  populateForm(): void {
    if (this.originalProduct) {
      this.productForm.patchValue({
        name: this.originalProduct.name,
        description: this.originalProduct.description,
        wholesalePrice: this.originalProduct.wholesalePrice,
        retailPrice: this.originalProduct.retailPrice,
        quantity: this.originalProduct.quantity,
      });
    }
  }

  saveChanges(): void {
    if (this.productForm.invalid) {
      this.errorMsg = 'Please correct the errors in the form.';
      return;
    }

    const updatedFields: any = {};
    const formValues = this.productForm.value;

    // compare form value with original
    for (const key in formValues) {
      if (formValues[key] !== this.originalProduct[key]) {
        updatedFields[key] = formValues[key];
      }
    }

    if (Object.keys(updatedFields).length === 0) {
      this.errorMsg = 'No changes detected.';
      return;
    }

    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http
      .patch<any>(
        `http://localhost:8080/products/${this.productId}`,
        updatedFields,
        { headers }
      )
      .subscribe(
        (response) => {
          if (response.success) {
            this.successMsg = 'Product updated successfully.';
            this.errorMsg = '';
            this.router.navigate(['/admin/product']);
          } else {
            this.errorMsg = response.message;
            this.successMsg = '';
          }
        },
        (error) => {
          this.errorMsg = 'Failed to update product details.';
          this.successMsg = '';
        }
      );
  }

  disregardChanges(): void {
    this.router.navigate(['/admin/product']); // Redirect to admin/product
  }
}
