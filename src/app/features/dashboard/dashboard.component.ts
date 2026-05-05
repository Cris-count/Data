import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CurrencyPipe } from '@angular/common';
import { AnalyticsService } from '../../core/services/analytics.service';
import { SalesPredictionService } from '../../core/services/sales-prediction.service';
import { SalesSummary } from '../../shared/models/sales.model';
import { SalesPrediction } from '../../shared/models/prediction.model';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  summary = signal<SalesSummary | null>(null);
  lastPrediction = signal<SalesPrediction | null>(null);

  constructor(private analytics: AnalyticsService, private predictions: SalesPredictionService) {}

  ngOnInit(): void {
    this.analytics.summary().subscribe((summary) => this.summary.set(summary));
    this.predictions.list().subscribe((items) => this.lastPrediction.set(items[0] ?? null));
  }
}
