import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LogEntry {
  id: number;
  timestamp: string;
  level: 'DEBUG' | 'INFO' | 'WARN' | 'ERROR' | 'FATAL';
  source: string;
  message: string;
}

@Injectable({ providedIn: 'root' })
export class LogsService {
  private readonly API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getAll(): Observable<LogEntry[]> {
    return this.http.get<LogEntry[]>(`${this.API}/logs`);
  }
}
