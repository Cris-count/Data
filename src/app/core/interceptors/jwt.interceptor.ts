import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (request, next) => {
  if (typeof localStorage === 'undefined') {
    return next(request);
  }
  const token = localStorage.getItem('data_jwt_token');
  if (!token || request.url.includes('/api/auth/login') || request.url.includes('/api/auth/register')) {
    return next(request);
  }
  return next(request.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
