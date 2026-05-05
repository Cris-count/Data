import { Component, OnInit, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { SalesPredictionService } from '../../../core/services/sales-prediction.service';
import { SalesPrediction } from '../../../shared/models/prediction.model';

@Component({
  selector: 'app-prediction-list',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './prediction-list.component.html'
})
export class PredictionListComponent implements OnInit {
  predictions = signal<SalesPrediction[]>([]);
  latest = signal<SalesPrediction | null>(null);
  loading = signal(false);

  constructor(private predictionService: SalesPredictionService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.predictionService.list().subscribe((items) => {
      this.predictions.set(items);
      this.latest.set(items[0] ?? null);
    });
  }

  generate(): void {
    this.loading.set(true);
    this.predictionService.generateNextMonth().subscribe((prediction) => {
      this.loading.set(false);
      this.latest.set(prediction);
      this.load();
    });
  }

  remove(id: number): void {
    this.predictionService.delete(id).subscribe(() => this.load());
  }
}
