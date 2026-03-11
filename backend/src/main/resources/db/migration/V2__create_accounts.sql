create table accounts (
  id bigserial primary key,
  user_id bigint not null references users(id),
  name varchar(150) not null,
  type varchar(20) not null,
  currency varchar(10) not null,
  balance numeric(19,4) not null default 0,
  color varchar(100),
  card_network varchar(30),
  card_number_masked varchar(50),
  card_number_full varchar(255),
  expiry_date varchar(10),
  included_in_balance boolean not null default true,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now(),
  deleted_at timestamp
);
