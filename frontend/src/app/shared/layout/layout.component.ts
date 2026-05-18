import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

const NAV = [
  { id: 'dashboard', label: 'Dashboard',  route: '/dashboard'  },
  { id: 'logs',      label: 'Logs',       route: '/logs'       },
  { id: 'alerts',    label: 'Alerts',     route: '/alerts'     },
  { id: 'anomalies', label: 'Anomalies',  route: '/anomalies'  },
  { id: 'settings',  label: 'Settings',   route: '/settings'   },
];

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  navItems = NAV;

  constructor(public auth: AuthService) {}

  get userInitial(): string {
    return (this.auth.currentUser()?.username?.[0] ?? 'U').toUpperCase();
  }

  logout(): void {
    this.auth.logout();
  }
}
