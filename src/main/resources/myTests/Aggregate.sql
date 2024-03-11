create table aggregate as select generate_series(1, 10000) as col_1;

insert into aggregate
select generate_series(1, 5000) union all select generate_series(1, 5000) as col_2;