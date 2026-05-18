import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Anomaly {
  id: number;
  detectedAt: string;
  type: 'KEYWORD_MATCH' | 'ERROR_RATE_SPIKE' | 'REPEATED_MESSAGE';
  description: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  triggerLogId: number;
}

@Injectable({ providedIn: 'root' })
export class AnomaliesService {
  private readonly API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Anomaly[]> {
    return this.http.get<Anomaly[]>(`${this.API}/anomalies`);
  }
}
