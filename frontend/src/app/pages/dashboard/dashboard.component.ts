import { Component, OnInit, inject } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { LogEntry } from '../../core/services/deploy.service';
import { Alert } from '../../core/services/incidents.service';
import { Anomaly } from '../../core/services/metrics.service';

const API = 'http://localhost:8080/api';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe, NgClass, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  private http = inject(HttpClient);

  loading = true;
  error = false;

  totalLogs = 0;
  totalAnomalies = 0;
  openAlerts = 0;
  criticalAnomalies = 0;

  recentLogs: LogEntry[] = [];
  recentAlerts: Alert[] = [];

  ngOnInit(): void {
    forkJoin({
      logs:      this.http.get<LogEntry[]>(`${API}/logs`),
      anomalies: this.http.get<Anomaly[]>(`${API}/anomalies`),
      alerts:    this.http.get<Alert[]>(`${API}/alerts`)
    }).subscribe({
      next: ({ logs, anomalies, alerts }) => {
        this.totalLogs        = logs.length;
        this.totalAnomalies   = anomalies.length;
        this.openAlerts       = alerts.filter(a => a.status === 'OPEN').length;
        this.criticalAnomalies = anomalies.filter(a => a.severity === 'CRITICAL').length;
        this.recentLogs       = logs.slice(-5).reverse();
        this.recentAlerts     = alerts.slice(-5).reverse();
        this.loading = false;
      },
      error: () => { this.loading = false; this.error = true; }
    });
  }

  levelTone(level: string): string {
    const m: Record<string, string> = { DEBUG: '', INFO: 'info', WARN: 'warn', ERROR: 'crit', FATAL: 'crit' };
    return m[level] ?? '';
  }

  severityTone(s: string): string {
    const m: Record<string, string> = { LOW: 'ok', MEDIUM: 'warn', HIGH: 'warn', CRITICAL: 'crit' };
    return m[s] ?? '';
  }

  statusTone(s: string): string {
    const m: Record<string, string> = { OPEN: 'crit', ACKNOWLEDGED: 'warn', RESOLVED: 'ok' };
    return m[s] ?? '';
  }
}
