import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent implements OnInit {
  auth = inject(AuthService);
  private http = inject(HttpClient);

  backendStatus: 'checking' | 'up' | 'down' = 'checking';

  ngOnInit(): void {
    this.http.get<{ status: string }>('http://localhost:8080/api/health').subscribe({
      next:  () => { this.backendStatus = 'up'; },
      error: () => { this.backendStatus = 'down'; }
    });
  }
}
