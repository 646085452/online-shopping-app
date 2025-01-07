import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class LoginGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      if (localStorage.getItem('role') === 'user') {
        this.router.navigate(['/user/home']);
      } else {
        this.router.navigate(['/admin/home']);
      }
      return false;
    }
    return true;
  }
}
