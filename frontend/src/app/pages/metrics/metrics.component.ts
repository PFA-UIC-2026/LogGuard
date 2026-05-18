import { Component, OnInit, inject } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { Anomaly, AnomaliesService } from '../../core/services/metrics.service';

const SEVERITIES = ['ALL', 'LOW', 'MEDIUM', 'HIGH', 'CRITICAL'] as const;

@Component({
  selector: 'app-metrics',
  standalone: true,
  imports: [DatePipe, NgClass],
  templateUrl: './metrics.component.html',
  styleUrl: './metrics.component.scss'
})
export class MetricsComponent implements OnInit {
  private svc = inject(AnomaliesService);

  loading = true;
  error = false;
  all: Anomaly[] = [];
  filtered: Anomaly[] = [];
  severities = SEVERITIES;
  activeSeverity: string = 'ALL';

  ngOnInit(): void {
    this.svc.getAll().subscribe({
      next: (data) => {
        this.all = data.slice().reverse();
        this.applyFilter();
        this.loading = false;
      },
      error: () => { this.loading = false; this.error = true; }
    });
  }

  setSeverity(s: string): void {
    this.activeSeverity = s;
    this.applyFilter();
  }

  private applyFilter(): void {
    this.filtered = this.activeSeverity === 'ALL'
      ? this.all
      : this.all.filter(a => a.severity === this.activeSeverity);
  }

  count(s: string): number {
    return s === 'ALL' ? this.all.length : this.all.filter(a => a.severity === s).length;
  }

  severityTone(s: string): string {
    const m: Record<string, string> = { LOW: 'ok', MEDIUM: 'warn', HIGH: 'warn', CRITICAL: 'crit' };
    return m[s] ?? '';
  }

  typeTone(t: string): string {
    return 'info';
  }

  typeLabel(t: string): string {
    const m: Record<string, string> = {
      KEYWORD_MATCH: 'Keyword Match',
      ERROR_RATE_SPIKE: 'Error Rate Spike',
      REPEATED_MESSAGE: 'Repeated Message'
    };
    return m[t] ?? t;
  }
}
