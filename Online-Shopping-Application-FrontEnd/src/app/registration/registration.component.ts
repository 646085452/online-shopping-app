import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
})
export class RegistrationComponent implements OnInit {
  registrationForm: FormGroup;
  errorMsg: string = '';
  successMsg: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registrationForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit() {}

  onRegister(): void {
    if (this.registrationForm.invalid) {
      this.errorMsg = 'Please fill out all required fields correctly.';
      return;
    }

    const userDetails = this.registrationForm.value;

    this.authService.register(userDetails).subscribe(
      (response) => {
        if (response.success) {
          this.successMsg = 'User registered successfully';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        } else {
          this.errorMsg = response.message;
        }
      },
      (error) => {
        this.errorMsg = 'An error occurred during registration';
      }
    );
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }
}
