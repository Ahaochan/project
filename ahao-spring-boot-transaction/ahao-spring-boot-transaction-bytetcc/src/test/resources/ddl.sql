drop table if exists ahao_account1;
create table ahao_account1(
    account_id varchar(16) primary key,
    amount decimal(10, 2),
    frozen decimal(10, 2)
) engine=innodb;
insert into ahao_account1(account_id, amount, frozen) values ('account1', 100.00, 0.00);

drop table if exists ahao_account2;
create table ahao_account2(
    account_id varchar(16) primary key,
    amount decimal(10, 2),
    frozen decimal(10, 2)
) engine=innodb;
insert into ahao_account2(account_id, amount, frozen) values ('account2', 100.00, 0.00);
