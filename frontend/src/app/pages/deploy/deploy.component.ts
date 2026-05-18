import { Component, OnInit, inject } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { Alert, AlertsService } from '../../core/services/incidents.service';

const STATUSES = ['ALL', 'OPEN', 'ACKNOWLEDGED', 'RESOLVED'] as const;

@Component({
  selector: 'app-deploy',
  standalone: true,
  imports: [DatePipe, NgClass],
  templateUrl: './deploy.component.html',
  styleUrl: './deploy.component.scss'
})
export class DeployComponent implements OnInit {
  private svc = inject(AlertsService);

  loading = true;
  error = false;
  all: Alert[] = [];
  filtered: Alert[] = [];
  statuses = STATUSES;
  activeStatus: string = 'ALL';
  actionLoading: number | null = null;

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next: (data) => {
        this.all = data.slice().reverse();
        this.applyFilter();
        this.loading = false;
      },
      error: () => { this.loading = false; this.error = true; }
    });
  }

  setStatus(s: string): void {
    this.activeStatus = s;
    this.applyFilter();
  }

  private applyFilter(): void {
    this.filtered = this.activeStatus === 'ALL'
      ? this.all
      : this.all.filter(a => a.status === this.activeStatus);
  }

  acknowledge(id: number): void {
    this.actionLoading = id;
    this.svc.acknowledge(id).subscribe({
      next: () => { this.actionLoading = null; this.load(); },
      error: () => { this.actionLoading = null; }
    });
  }

  resolve(id: number): void {
    this.actionLoading = id;
    this.svc.resolve(id).subscribe({
      next: () => { this.actionLoading = null; this.load(); },
      error: () => { this.actionLoading = null; }
    });
  }

  count(s: string): number {
    return s === 'ALL' ? this.all.length : this.all.filter(a => a.status === s).length;
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
