create table huge_table
(
    x integer
);

create table huge_table_1
(
    x integer
);

create table huge_table_2
(
    x integer
);

create table huge_parent_table
(
    id   integer primary key,
    data text
);

create table huge_users_table
(
    id integer primary key
);

create table huge_profile_table
(
    id   integer primary key references huge_users_table (id),
    data text
);

create table huge_child_table
(
    id        integer primary key,
    parent_id integer references huge_parent_table (id),
    data      text
);

create table huge_first_partner_table
(
    id integer primary key
);

create table huge_second_partner_table
(
    id integer primary key
);

create table huge_business_table
(
    id             integer primary key,
    first_partner  integer references huge_first_partner_table (id),
    second_partner integer references huge_second_partner_table (id),
    data text
);

insert into huge_table (x)
select generate_series(1, 1000000);

insert into huge_table_1 (x)
select floor(random() * (1000000-1+1) + 1)::int
from generate_series(1, 1000000);

insert into huge_table_2 (x)
select floor(random() * (1000000-1+1) + 1)::int
from generate_series(1, 1000000);

insert into huge_parent_table (id, data)
select generate_series,
       md5(random()::text)
from generate_series(1, 1000000);

insert into huge_child_table (id, parent_id, data)
select generate_series,
       floor(random() * 1000000) + 1,
       md5(random()::text)
from generate_series(1, 1000000);

insert into huge_users_table (id)
select generate_series(1, 1000000);

insert into huge_profile_table (id, data)
select generate_series,
       md5(random()::text)
from generate_series(1, 1000000);

insert into huge_first_partner_table (id)
select generate_series(1, 1000000);

insert into huge_second_partner_table (id)
select generate_series(1, 1000000);

insert into huge_business_table (id, first_partner, second_partner, data)
select generate_series,
       floor(random() * 1000000) + 1,
       floor(random() * 1000000) + 1,
       md5(random()::text)
from generate_series(1, 1000000);