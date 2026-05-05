import { Component, OnInit, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { AnalyticsBundle, AnalyticsService } from '../../../core/services/analytics.service';

@Component({
  selector: 'app-analytics-dashboard',
  imports: [CurrencyPipe],
  templateUrl: './analytics-dashboard.component.html'
})
export class AnalyticsDashboardComponent implements OnInit {
  data = signal<AnalyticsBundle | null>(null);
  maxRevenue = signal(1);

  constructor(private analytics: AnalyticsService) {}

  ngOnInit(): void {
    this.analytics.bundle().subscribe((data) => {
      this.data.set(data);
      this.maxRevenue.set(Math.max(1, ...data.monthly.map((item) => Number(item.revenue))));
    });
  }

  barHeight(value: number): string {
    return `${Math.max(8, (Number(value) / this.maxRevenue()) * 150)}px`;
  }
}
