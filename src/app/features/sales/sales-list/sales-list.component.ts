import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { SalesService } from '../../../core/services/sales.service';
import { SaleRecord, SaleRecordRequest } from '../../../shared/models/sales.model';

@Component({
  selector: 'app-sales-list',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './sales-list.component.html'
})
export class SalesListComponent implements OnInit {
  private fb = inject(FormBuilder);
  sales = signal<SaleRecord[]>([]);
  editing = signal<SaleRecord | null>(null);
  error = signal('');
  total = computed(() => this.sales().reduce((sum, sale) => sum + Number(sale.totalAmount), 0));

  form = this.fb.nonNullable.group({
    saleDate: ['', Validators.required],
    productName: ['', Validators.required],
    category: ['Tecnologia', Validators.required],
    unitsSold: [1, [Validators.required, Validators.min(0)]],
    unitPrice: [0, [Validators.required, Validators.min(0)]],
    salesChannel: ['E-commerce', Validators.required],
    region: ['Armenia', Validators.required],
    customerSegment: ['Retail', Validators.required]
  });

  filters = this.fb.nonNullable.group({
    category: [''],
    region: [''],
    channel: [''],
    from: [''],
    to: ['']
  });

  constructor(private salesService: SalesService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.salesService.list(this.filters.getRawValue()).subscribe({
      next: (sales) => this.sales.set(sales),
      error: (err) => this.error.set(err.error?.message ?? 'No se pudieron cargar las ventas')
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const payload = this.form.getRawValue() as SaleRecordRequest;
    const request = this.editing()
      ? this.salesService.update(this.editing()!.id, payload)
      : this.salesService.create(payload);
    request.subscribe({
      next: () => {
        this.reset();
        this.load();
      },
      error: (err) => this.error.set(err.error?.message ?? 'No se pudo guardar la venta')
    });
  }

  edit(sale: SaleRecord): void {
    this.editing.set(sale);
    this.form.patchValue({
      saleDate: sale.saleDate,
      productName: sale.productName,
      category: sale.category,
      unitsSold: sale.unitsSold,
      unitPrice: Number(sale.unitPrice),
      salesChannel: sale.salesChannel,
      region: sale.region,
      customerSegment: sale.customerSegment
    });
  }

  remove(id: number): void {
    this.salesService.delete(id).subscribe(() => this.load());
  }

  reset(): void {
    this.editing.set(null);
    this.form.reset({
      saleDate: '',
      productName: '',
      category: 'Tecnologia',
      unitsSold: 1,
      unitPrice: 0,
      salesChannel: 'E-commerce',
      region: 'Armenia',
      customerSegment: 'Retail'
    });
  }
}
