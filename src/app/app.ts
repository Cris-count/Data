import { Component, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  theme = signal<'light' | 'dark'>(this.readTheme());
  menuOpen = signal(false);

  constructor(public auth: AuthService, private router: Router) {}

  isPublicRoute(): boolean {
    return this.router.url.startsWith('/login') || this.router.url.startsWith('/register');
  }

  toggleTheme(): void {
    const next = this.theme() === 'light' ? 'dark' : 'light';
    this.theme.set(next);
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('data_theme', next);
    }
  }

  toggleMenu(): void {
    this.menuOpen.update((open) => !open);
  }

  closeMenu(): void {
    this.menuOpen.set(false);
  }

  private readTheme(): 'light' | 'dark' {
    if (typeof localStorage === 'undefined') return 'light';
    return localStorage.getItem('data_theme') === 'dark' ? 'dark' : 'light';
  }
}
