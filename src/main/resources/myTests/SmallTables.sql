create table small_table
(
    x integer
);

create table small_parent_table
(
    id   integer primary key,
    data text
);

create table small_users_table
(
    id integer primary key
);

create table small_profile_table
(
    id   integer primary key references small_users_table (id),
    data text
);

create table small_child_table
(
    id        integer primary key,
    parent_id integer references small_parent_table (id),
    data      text
);

create table small_first_partner_table
(
    id integer primary key
);

create table small_second_partner_table
(
    id integer primary key
);

create table small_business_table
(
    id             integer primary key,
    first_partner  integer references small_first_partner_table (id),
    second_partner integer references small_second_partner_table (id),
    data text
);

insert into small_table (x)
select generate_series(1, 10);

insert into small_parent_table (id, data)
select generate_series,
       md5(random()::text)
from generate_series(1, 10);

insert into small_child_table (id, parent_id, data)
select generate_series,
       floor(random() * 10) + 1,
       md5(random()::text)
from generate_series(1, 10);

insert into small_users_table (id)
select generate_series(1, 10);

insert into small_profile_table (id, data)
select generate_series,
       md5(random()::text)
from generate_series(1, 10);

insert into small_first_partner_table (id)
select generate_series(1, 10);

insert into small_second_partner_table (id)
select generate_series(1, 10);

insert into small_business_table (id, first_partner, second_partner, data)
select generate_series,
       floor(random() * 10) + 1,
       floor(random() * 10) + 1,
       md5(random()::text)
from generate_series(1, 10);