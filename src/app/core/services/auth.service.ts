import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { API_BASE_URL } from './api.config';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../shared/models/auth.model';

const TOKEN_KEY = 'data_jwt_token';
const USER_KEY = 'data_auth_user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  currentUser = signal<AuthResponse | null>(this.readUser());

  constructor(private http: HttpClient, private router: Router) {}

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_BASE_URL}/auth/login`, payload).pipe(tap((response) => this.saveSession(response)));
  }

  register(payload: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${API_BASE_URL}/auth/register`, payload).pipe(tap((response) => this.saveSession(response)));
  }

  me(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${API_BASE_URL}/auth/me`).pipe(tap((response) => this.saveSession(response)));
  }

  token(): string | null {
    if (typeof localStorage === 'undefined') return null;
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.token();
  }

  logout(): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(USER_KEY);
    }
    this.currentUser.set(null);
    this.router.navigateByUrl('/login');
  }

  private saveSession(response: AuthResponse): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(TOKEN_KEY, response.token);
      localStorage.setItem(USER_KEY, JSON.stringify(response));
    }
    this.currentUser.set(response);
  }

  private readUser(): AuthResponse | null {
    if (typeof localStorage === 'undefined') return null;
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) as AuthResponse : null;
  }
}
