# LogGuard API Contract

Base URL: `http://<host>:8080`

All protected endpoints require the header:
```
Authorization: Bearer <token>
```

---

## Authentication

### POST `/api/auth/login`
> **Public** — no token required

**Request body:**
```json
{
  "username": "admin",
  "password": "secret123"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "username": "admin",
    "role": "ADMIN"
  }
}
```

**Response `401 Unauthorized`:**
```json
{
  "error": "Invalid credentials"
}
```

---

### GET `/api/auth/me`
> **Protected**

Returns the currently authenticated user.

**Response `200 OK`:**
```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

**Response `401 Unauthorized`:**
```json
{
  "error": "Token missing or invalid"
}
```

---

## Logs

### GET `/api/logs`
> **Protected**

Returns all ingested log entries.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "timestamp": "2026-05-05T10:23:00",
    "level": "ERROR",
    "source": "auth-service",
    "message": "Login attempt failed for user root"
  },
  {
    "id": 2,
    "timestamp": "2026-05-05T10:24:15",
    "level": "WARN",
    "source": "api-gateway",
    "message": "Rate limit exceeded on /api/users"
  }
]
```

---

### POST `/api/logs`
> **Protected**

Ingests a new log entry.

**Request body:**
```json
{
  "level": "ERROR",
  "source": "auth-service",
  "message": "Login attempt failed for user root",
  "rawPayload": "{\"ip\":\"192.168.1.50\",\"attempt\":5}"
}
```

`level` accepted values: `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`

**Response `200 OK`:**
```json
{
  "id": 3,
  "timestamp": "2026-05-05T10:30:00",
  "level": "ERROR",
  "source": "auth-service",
  "message": "Login attempt failed for user root"
}
```

---

## Anomalies

### GET `/api/anomalies`
> **Protected**

Returns all detected anomalies.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "detectedAt": "2026-05-05T10:25:00",
    "type": "BRUTE_FORCE",
    "description": "5 failed login attempts from 192.168.1.50 within 60 seconds",
    "severity": "HIGH",
    "triggerLogId": 1
  },
  {
    "id": 2,
    "detectedAt": "2026-05-05T10:26:00",
    "type": "RATE_LIMIT_VIOLATION",
    "description": "Excessive requests to /api/users",
    "severity": "MEDIUM",
    "triggerLogId": 2
  }
]
```

`severity` values: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`

---

## Alerts

### GET `/api/alerts`
> **Protected**

Returns all alerts. Optionally filter by status.

**Query parameters:**

| Param | Required | Values |
|-------|----------|--------|
| `status` | No | `OPEN`, `ACKNOWLEDGED`, `RESOLVED` |

**Example:** `GET /api/alerts?status=OPEN`

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "createdAt": "2026-05-05T10:25:01",
    "severity": "HIGH",
    "message": "Brute force attack detected from 192.168.1.50",
    "status": "OPEN",
    "anomalyId": 1
  },
  {
    "id": 2,
    "createdAt": "2026-05-05T10:26:02",
    "severity": "MEDIUM",
    "message": "Rate limit violation on /api/users",
    "status": "ACKNOWLEDGED",
    "anomalyId": 2
  }
]
```

---

### PUT `/api/alerts/{id}/acknowledge`
> **Protected**

Marks an alert as acknowledged.

**Example:** `PUT /api/alerts/1/acknowledge`

**Response `200 OK`:**
```json
{
  "id": 1,
  "createdAt": "2026-05-05T10:25:01",
  "severity": "HIGH",
  "message": "Brute force attack detected from 192.168.1.50",
  "status": "ACKNOWLEDGED",
  "anomalyId": 1
}
```

**Response `404 Not Found`:**
```json
{
  "error": "Alert not found"
}
```

---

### PUT `/api/alerts/{id}/resolve`
> **Protected**

Marks an alert as resolved.

**Example:** `PUT /api/alerts/1/resolve`

**Response `200 OK`:**
```json
{
  "id": 1,
  "createdAt": "2026-05-05T10:25:01",
  "severity": "HIGH",
  "message": "Brute force attack detected from 192.168.1.50",
  "status": "RESOLVED",
  "anomalyId": 1
}
```

**Response `404 Not Found`:**
```json
{
  "error": "Alert not found"
}
```

---

## Health

### GET `/api/health`
> **Public** — no token required

**Response `200 OK`:**
```json
{
  "status": "UP",
  "service": "logguard-backend"
}
```

---

## Notes for the frontend team

- Store the JWT token in `localStorage` or `sessionStorage` after login.
- Attach it to every protected request: `Authorization: Bearer <token>`
- Token expires after `86400` seconds (24h) — redirect to login on `401`.
- `POST /api/auth/login` and `GET /api/health` are the only public endpoints.
- All endpoints are implemented. Auth is enforced — every request to `/api/logs`, `/api/anomalies`, and `/api/alerts` requires a valid token.
