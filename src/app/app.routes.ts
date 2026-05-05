import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { SalesListComponent } from './features/sales/sales-list/sales-list.component';
import { AnalyticsDashboardComponent } from './features/analytics/analytics-dashboard/analytics-dashboard.component';
import { PredictionListComponent } from './features/predictions/prediction-list/prediction-list.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'sales', component: SalesListComponent, canActivate: [authGuard] },
  { path: 'analytics', component: AnalyticsDashboardComponent, canActivate: [authGuard] },
  { path: 'predictions', component: PredictionListComponent, canActivate: [authGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: '**', redirectTo: 'dashboard' }
];
