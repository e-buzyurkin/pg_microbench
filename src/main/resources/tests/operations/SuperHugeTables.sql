create table if not exists super_huge_table (
   x integer
);

insert into super_huge_table(x)
select generate_series(1, 100000000);