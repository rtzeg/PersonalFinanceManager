create table exchange_rates (
  id bigserial primary key,
  from_currency varchar(10) not null,
  to_currency varchar(10) not null,
  rate numeric(19,8) not null,
  source varchar(50),
  updated_at timestamp not null default now(),
  unique (from_currency, to_currency)
);
