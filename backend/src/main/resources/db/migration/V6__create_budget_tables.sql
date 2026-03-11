create table budget_plans (
  id bigserial primary key,
  user_id bigint not null references users(id),
  month_key varchar(7) not null,
  currency varchar(10) not null,
  planned_income_total numeric(19,4) not null default 0,
  planned_expense_total numeric(19,4) not null default 0,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  unique (user_id, month_key)
);

create table budget_income_plan_items (
  id bigserial primary key,
  budget_plan_id bigint not null references budget_plans(id) on delete cascade,
  category varchar(100) not null,
  planned_amount numeric(19,4) not null default 0,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  unique (budget_plan_id, category)
);

create table budget_category_limits (
  id bigserial primary key,
  budget_plan_id bigint not null references budget_plans(id) on delete cascade,
  category varchar(100) not null,
  limit_amount numeric(19,4) not null default 0,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  unique (budget_plan_id, category)
);
