create table small (
    col_1 integer
);

create table medium (
    col_1 integer
);

create table large (
    col_1 integer
);

create table huge (
    col_1 integer
);

insert into small (col_1)
select generate_series(1, 5);
insert into medium (col_1)
select generate_series(1, 1000);
insert into large (col_1)
select generate_series(1, 100000);
insert into huge (col_1)
select generate_series(1, 5000000);