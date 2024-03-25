create table large_table
(
    x integer
);

create table large_parent_table
(
    id   integer primary key,
    data text
);

create table large_users_table
(
    id integer primary key
);

create table large_profile_table
(
    id   integer primary key references large_users_table (id),
    data text
);

create table large_child_table
(
    id        integer primary key,
    parent_id integer references large_parent_table (id),
    data      text
);

create table large_first_partner_table
(
    id integer primary key
);

create table large_second_partner_table
(
    id integer primary key
);

create table large_business_table
(
    id             integer primary key,
    first_partner  integer references large_first_partner_table (id),
    second_partner integer references large_second_partner_table (id),
    data text
);

insert into large_table (x)
select generate_series(1, 100000);

insert into large_parent_table (id, data)
select generate_series,
       md5(random()::text)
from generate_series(1, 100000);

insert into large_child_table (id, parent_id, data)
select generate_series,
       floor(random() * 100000) + 1,
       md5(random()::text)
from generate_series(1, 100000);

insert into large_users_table (id)
select generate_series(1, 100000);

insert into large_profile_table (id, data)
select generate_series,
       md5(random()::text)
from generate_series(1, 100000);

insert into large_first_partner_table (id)
select generate_series(1, 100000);

insert into large_second_partner_table (id)
select generate_series(1, 100000);

insert into large_business_table (id, first_partner, second_partner, data)
select generate_series,
       floor(random() * 100000) + 1,
       floor(random() * 100000) + 1,
       md5(random()::text)
from generate_series(1, 100000);