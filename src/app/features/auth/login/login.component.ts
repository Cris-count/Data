import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  error = signal('');
  loading = signal(false);
  form = this.fb.nonNullable.group({
    email: ['demo@data.com', [Validators.required, Validators.email]],
    password: ['Demo1234', [Validators.required]]
  });

  constructor(private auth: AuthService, private router: Router) {}

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set('');
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigateByUrl('/dashboard'),
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message ?? 'No fue posible iniciar sesion');
      }
    });
  }
}
