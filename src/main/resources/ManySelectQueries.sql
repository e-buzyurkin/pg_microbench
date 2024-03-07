select format(E'drop table t_tab_%s;', g.id) as cmd
  from generate_series(1, 10000) as g(id)
\gexec

select format(E'CREATE TABLE t_tab_%s as select id::bigint as id, repeat(\'x\', 50) as val, md5(id::text || \'name\') as c1, id / 100 as c2, mod(id, 100) as c3 from generate_series(1,1000) id;', g.id) as cmd
  from generate_series(1, 10000) as g(id)
\gexec

select format(E'create unique index on t_tab_%s (id);', g.id) as cmd
  from generate_series(1, 10000) as g(id)
\gexec

select format(E'create index on t_tab_%s (c2, c3);', g.id) as cmd
  from generate_series(1, 10000) as g(id)
\gexec

vacuum freeze analyze;
