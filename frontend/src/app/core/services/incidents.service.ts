import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Alert {
  id: number;
  createdAt: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  message: string;
  status: 'OPEN' | 'ACKNOWLEDGED' | 'RESOLVED';
  anomalyId: number;
}

@Injectable({ providedIn: 'root' })
export class AlertsService {
  private readonly API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getAll(status?: string): Observable<Alert[]> {
    return status
      ? this.http.get<Alert[]>(`${this.API}/alerts`, { params: { status } })
      : this.http.get<Alert[]>(`${this.API}/alerts`);
  }

  acknowledge(id: number): Observable<Alert> {
    return this.http.put<Alert>(`${this.API}/alerts/${id}/acknowledge`, {});
  }

  resolve(id: number): Observable<Alert> {
    return this.http.put<Alert>(`${this.API}/alerts/${id}/resolve`, {});
  }
}
