export interface PerformanceResult {
    operation: string;
    database: string;
    recordCount: number;
    durationMs: number;
    timestamp: string;
    details: string;
}
