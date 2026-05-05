export interface SaleRecord {
  id: number;
  saleDate: string;
  productName: string;
  category: string;
  unitsSold: number;
  unitPrice: number;
  totalAmount: number;
  salesChannel: string;
  region: string;
  customerSegment: string;
  createdAt: string;
}

export interface SaleRecordRequest {
  saleDate: string;
  productName: string;
  category: string;
  unitsSold: number;
  unitPrice: number;
  salesChannel: string;
  region: string;
  customerSegment: string;
}

export interface SalesSummary {
  totalSales: number;
  totalRevenue: number;
  totalUnits: number;
  averageTicket: number;
  topProduct: string;
  mostProfitableCategory: string;
  bestRegion: string;
  strongestChannel: string;
}

export interface MonthlySales {
  month: string;
  revenue: number;
  units: number;
}

export interface CategorySales {
  category: string;
  revenue: number;
  units: number;
}

export interface RegionSales {
  region: string;
  revenue: number;
  units: number;
}

export interface TopProduct {
  productName: string;
  revenue: number;
  units: number;
}
