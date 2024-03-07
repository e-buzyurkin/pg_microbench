create or replace procedure create_tmp()
as $$
begin
  if exists (
    select 1
      from pg_tables pt inner join pg_namespace pn
        on pn.nspname = pt.schemaname
      where tablename = 'tt1'
        and pn.oid = pg_my_temp_schema())
  then
    return;
  end if;

  for i in 1..40 loop
    execute format(E'drop table if exists tt%s;', i);
    execute format(E'create temporary table tt%s (
        id bigserial primary key,
        val text,
        c1 text,
        c2 int4,
        c3 int4
    ) without oids;', i);
  end loop;

  create temporary table tt41 (
        id bigserial primary key,
        val text,
        c1 text,
        c2 int4,
        c3 int4
    ) without oids;

    insert into tt41(val, c1, c2, c3)
      select repeat('x', 50)::text as val,
         md5(id::text || 'name') as c1,
         id / 100 as c2, mod(id, 100) as c3
      from generate_series(1, 1000000) id;

  commit;
end;
$$ 
language plpgsql;

