# API Layer

Фронтенд подключен к реальному backend API (`Spring Boot`) через `fetch` + `credentials: include`.

## Конфиг

`src/api/api.ts` использует:
- `VITE_API_BASE_URL` (по умолчанию пусто, т.е. same-origin `/api` )
- авто-инициализацию сессии через `/api/auth/me` и demo login/register.

Пример `.env`:

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_DEMO_USERNAME=demo
VITE_DEMO_PASSWORD=demo123
```

## Что реализовано

- Accounts: GET/POST/PUT/DELETE
- Transactions: GET/POST/PUT/DELETE
- Debts: GET/POST/PUT/DELETE
- Notifications: GET
- Exchange rates: GET/PUT

Все DTO маппятся между backend-форматом и UI-форматом прямо в `api.ts`.
