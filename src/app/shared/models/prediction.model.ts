export type TrendLevel = 'LOW' | 'STABLE' | 'GROWING' | 'HIGH_GROWTH' | 'DECREASING';
export type ConfidenceLevel = 'LOW' | 'MEDIUM' | 'HIGH';

export interface SalesPrediction {
  id: number;
  predictionDate: string;
  targetMonth: string;
  predictedRevenue: number;
  predictedUnits: number;
  trendLevel: TrendLevel;
  confidenceLevel: ConfidenceLevel;
  predictionMessage: string;
  recommendedAction: string;
  createdAt: string;
}
