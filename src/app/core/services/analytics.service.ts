import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';
import { API_BASE_URL } from './api.config';
import { CategorySales, MonthlySales, RegionSales, SalesSummary, TopProduct } from '../../shared/models/sales.model';

export interface AnalyticsBundle {
  summary: SalesSummary;
  monthly: MonthlySales[];
  categories: CategorySales[];
  regions: RegionSales[];
  products: TopProduct[];
}

@Injectable({ providedIn: 'root' })
export class AnalyticsService {
  constructor(private http: HttpClient) {}

  summary(): Observable<SalesSummary> {
    return this.http.get<SalesSummary>(`${API_BASE_URL}/analytics/summary`);
  }

  monthlySales(): Observable<MonthlySales[]> {
    return this.http.get<MonthlySales[]>(`${API_BASE_URL}/analytics/monthly-sales`);
  }

  byCategory(): Observable<CategorySales[]> {
    return this.http.get<CategorySales[]>(`${API_BASE_URL}/analytics/by-category`);
  }

  byRegion(): Observable<RegionSales[]> {
    return this.http.get<RegionSales[]>(`${API_BASE_URL}/analytics/by-region`);
  }

  topProducts(): Observable<TopProduct[]> {
    return this.http.get<TopProduct[]>(`${API_BASE_URL}/analytics/top-products`);
  }

  bundle(): Observable<AnalyticsBundle> {
    return forkJoin({
      summary: this.summary(),
      monthly: this.monthlySales(),
      categories: this.byCategory(),
      regions: this.byRegion(),
      products: this.topProducts()
    });
  }
}
