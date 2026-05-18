import { Component, OnInit, inject } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { LogEntry, LogsService } from '../../core/services/deploy.service';

const LEVELS = ['ALL', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL'] as const;

@Component({
  selector: 'app-incidents',
  standalone: true,
  imports: [DatePipe, NgClass],
  templateUrl: './incidents.component.html',
  styleUrl: './incidents.component.scss'
})
export class IncidentsComponent implements OnInit {
  private svc = inject(LogsService);

  loading = true;
  error = false;
  all: LogEntry[] = [];
  filtered: LogEntry[] = [];
  levels = LEVELS;
  activeLevel: string = 'ALL';

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

  setLevel(level: string): void {
    this.activeLevel = level;
    this.applyFilter();
  }

  private applyFilter(): void {
    this.filtered = this.activeLevel === 'ALL'
      ? this.all
      : this.all.filter(e => e.level === this.activeLevel);
  }

  levelTone(level: string): string {
    const m: Record<string, string> = { DEBUG: '', INFO: 'info', WARN: 'warn', ERROR: 'crit', FATAL: 'crit' };
    return m[level] ?? '';
  }

  count(level: string): number {
    return level === 'ALL' ? this.all.length : this.all.filter(e => e.level === level).length;
  }
}
