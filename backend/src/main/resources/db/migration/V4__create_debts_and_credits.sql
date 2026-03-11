create table debts (
  id bigserial primary key,
  user_id bigint not null references users(id),
  name varchar(150) not null,
  amount numeric(19,4) not null,
  currency varchar(10) not null,
  type varchar(20) not null,
  status varchar(20) not null,
  description varchar(255),
  debt_date date not null,
  closed_at timestamp,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now()
);

create table credits (
  id bigserial primary key,
  user_id bigint not null references users(id),
  title varchar(150) not null,
  total_amount numeric(19,4) not null,
  currency varchar(10) not null,
  kind varchar(20) not null,
  start_date date not null,
  end_date date not null,
  months integer not null,
  paid_installments integer not null default 0,
  status varchar(20) not null,
  description varchar(255),
  created_at timestamp not null default now(),
  updated_at timestamp not null default now()
);
