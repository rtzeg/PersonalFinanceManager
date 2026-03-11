# PersonalFinanceManager Backend (Spring Boot)

REST backend for the existing frontend.

## Stack
- Java 21
- Spring Boot (Web, Security, Data JPA, Validation)
- PostgreSQL
- Flyway migrations
- Session-based auth (no JWT)

## Main notes implemented
- Required package layout under `src/main/java/com/pfm/...`
- `occurred_at` is a dedicated transaction operation datetime and used in filters/analytics.
- Money-changing operations in `TransactionService` are `@Transactional`.
- Transaction update/delete rollback previous balance effects before applying new state.
- Budget has **three tables**: `budget_plans`, `budget_income_plan_items`, `budget_category_limits`.
- Budget summary endpoint with rule-based warning engine.

## Run
1. Create PostgreSQL DB `pfm`.
2. Set env vars if needed: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
3. Run: `mvn spring-boot:run` from `backend/`.

## Implemented endpoints
- Auth: `/api/auth/register`, `/api/auth/login`, `/api/auth/me`
- Accounts CRUD + toggle in balance
- Transactions CRUD + filters `type`, `accountId`, `month`
- Debts CRUD + close
- Credits CRUD + installments increment
- Exchange rates list/update
- Budgets + income items + category limits + computed summary
- Notifications list/read/read-all
- Analytics overview/categories/income-vs-expense/balance-trend/budget-vs-actual


## Docker

Из корня проекта:

```sh
docker compose up --build
```

Backend поднимется на `http://localhost:8080`, БД будет подключена автоматически через:
- `DB_URL=jdbc:postgresql://db:5432/pfm`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=postgres`
