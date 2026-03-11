create table notifications (
  id bigserial primary key,
  user_id bigint not null references users(id),
  title varchar(150) not null,
  message text not null,
  type varchar(20) not null,
  read boolean not null default false,
  created_at timestamp not null default now()
);
