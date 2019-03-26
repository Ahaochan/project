drop table if exists SYS_USER;
create table SYS_USER(
  id       int,
  username varchar(500),
  email    varchar(500),
  password varchar(500),
  salt     varchar(500),
  locked   varchar(500),
  disabled varchar(500),
  expired  datetime
);
insert into SYS_USER(id, username, email, password, salt, locked, disabled, expired)
values (1, 'admin', 'admin@ahao.com', 'edfcc4776392b62693e975996a1f3a63a6de3bee81f43ba840b5721c10a2e65822ddf43b8dc43cd3dd6108a6c66771ac3b582dd1bb27d2ecdde5df6853b77d36', 'qwe', 0, 0, '2100-01-01');