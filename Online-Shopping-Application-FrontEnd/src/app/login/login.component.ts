import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMsg: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit() {}

  onLogin(): void {
    if (this.loginForm.invalid) {
      this.errorMsg = 'Please fill out all required fields';
      return;
    }

    const credentials = this.loginForm.value;
    this.authService.login(credentials).subscribe(
      (response) => {
        if (response.token) {
          this.authService.storeToken(response.token);
          this.authService.storeRole(response.role);
          if (response.role === 'user') {
            this.router.navigate(['/user/home']);
            // if (response.role === 'user') {
            //   console.log('Navigating to /user/home...');
            //   this.router.navigate(['/user/home']).then((success) => {
            //     if (success) {
            //       console.log('Navigation to /user/home was successful');
            //     } else {
            //       console.log('Navigation to /user/home failed');
            //     }
            //   });
          } else {
            this.router.navigate(['/admin/home']);
          }
        }
      },
      (error) => {
        if (error.status === 401) {
          this.errorMsg = 'Invalid username or password';
        }
      }
    );
  }

  navigateToRegister(): void {
    this.router.navigate(['/register']);
  }
}
