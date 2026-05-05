import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from './api.config';
import { SaleRecord, SaleRecordRequest } from '../../shared/models/sales.model';

export interface SaleFilters {
  category?: string;
  region?: string;
  channel?: string;
  from?: string;
  to?: string;
}

@Injectable({ providedIn: 'root' })
export class SalesService {
  constructor(private http: HttpClient) {}

  list(filters: SaleFilters = {}): Observable<SaleRecord[]> {
    let params = new HttpParams();
    Object.entries(filters).forEach(([key, value]) => {
      if (value) params = params.set(key, value);
    });
    return this.http.get<SaleRecord[]>(`${API_BASE_URL}/sales`, { params });
  }

  create(payload: SaleRecordRequest): Observable<SaleRecord> {
    return this.http.post<SaleRecord>(`${API_BASE_URL}/sales`, payload);
  }

  update(id: number, payload: SaleRecordRequest): Observable<SaleRecord> {
    return this.http.put<SaleRecord>(`${API_BASE_URL}/sales/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/sales/${id}`);
  }
}
