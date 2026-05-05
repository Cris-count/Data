import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from './api.config';
import { SalesPrediction } from '../../shared/models/prediction.model';

@Injectable({ providedIn: 'root' })
export class SalesPredictionService {
  constructor(private http: HttpClient) {}

  generateNextMonth(): Observable<SalesPrediction> {
    return this.http.post<SalesPrediction>(`${API_BASE_URL}/predictions/sales/next-month`, {});
  }

  list(): Observable<SalesPrediction[]> {
    return this.http.get<SalesPrediction[]>(`${API_BASE_URL}/predictions/sales`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/predictions/sales/${id}`);
  }
}
