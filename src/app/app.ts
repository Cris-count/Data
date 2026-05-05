import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(public auth: AuthService, private router: Router) {}

  isPublicRoute(): boolean {
    return this.router.url.startsWith('/login') || this.router.url.startsWith('/register');
  }
}
