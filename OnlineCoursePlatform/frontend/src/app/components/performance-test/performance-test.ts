import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { PerformanceResult } from '../../models/performance-result';

@Component({
  selector: 'app-performance-test',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './performance-test.html',
  styleUrl: './performance-test.css'
})
export class PerformanceTest {
  private apiService = inject(ApiService);

  results: PerformanceResult[] = [];
  loading = false;
  error: string | null = null;

  runTests() {
    this.loading = true;
    this.error = null;
    this.results = [];

    this.apiService.runPerformanceTests().subscribe({
      next: (data) => {
        this.results = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error running tests', err);
        this.error = 'Failed to run performance tests. Please try again.';
        this.loading = false;
      }
    });
  }

  get groupedResults() {
    const groups: { [key: string]: PerformanceResult[] } = {};
    this.results.forEach(r => {
      if (!groups[r.operation]) {
        groups[r.operation] = [];
      }
      groups[r.operation].push(r);
    });
    return Object.entries(groups);
  }
}
