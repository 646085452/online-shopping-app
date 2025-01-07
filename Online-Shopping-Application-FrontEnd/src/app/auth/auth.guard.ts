import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      console.log('AuthGuard: Token found, allowing navigation');
      return true;
    } else {
      console.log('AuthGuard: No token found, redirecting to /login');
      this.router.navigate(['/login']);
      return false;
    }
  }
}
