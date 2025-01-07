import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private loginUrl = 'http://localhost:8080/login';
  private registrationUrl = 'http://localhost:8080/signup';

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post(this.loginUrl, credentials);
  }

  register(userDetails: {
    username: string;
    email: string;
    password: string;
  }): Observable<any> {
    return this.http.post(this.registrationUrl, userDetails);
  }

  storeToken(token: string): void {
    localStorage.setItem('jwtToken', token);
  }

  storeRole(role: string): void {
    localStorage.setItem('role', role);
  }

  getToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  clearToken(): void {
    localStorage.removeItem('jwtToken');
  }
}
