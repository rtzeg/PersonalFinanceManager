create table transactions (
  id bigserial primary key,
  user_id bigint not null references users(id),
  type varchar(20) not null,
  amount numeric(19,4) not null,
  currency varchar(10) not null,
  category varchar(100) not null,
  description varchar(255) not null,
  note text,
  account_id bigint not null references accounts(id),
  to_account_id bigint references accounts(id),
  to_currency varchar(10),
  to_amount numeric(19,4),
  occurred_at timestamp not null,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  deleted_at timestamp
);
create index idx_transactions_user_occurred on transactions(user_id, occurred_at);
