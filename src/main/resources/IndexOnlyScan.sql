\set ON_ERROR_STOP off
create user benchuser with password 'benchuser';
create database benchmark with owner = benchuser;

\set ON_ERROR_STOP on
\c benchmark
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
\c benchmark benchuser

create table if not exists pg_indexonlyscan
(hash bigint, id uuid, value text, numvalue numeric, datevalue timestamp with time zone, status boolean);

insert into pg_indexonlyscan
(hash, id, value, numvalue, datevalue, status)
select
	(random() * 1000000)::bigint as hash,
    gen_random_uuid() as id,
    md5(random()::text) as "value",
    10000 * random() as numvalue,
    timestamp '2001-01-01 00:00:00' + random() * (timestamp '2020-01-01 00:00:00' - timestamp '2001-01-01 00:00:00') as datevalue,
	true as status
from generate_series(1, 10000000) s(i),
pg_class c
where c.relname = 'pg_indexonlyscan' and c.reltuples < 100;

create index if not exists pg_indexonlyscan_hash on pg_indexonlyscan (hash, status, id);

vacuum freeze pg_indexonlyscan;

create table if not exists pg_indexonlyscan_small
(hash bigint, id uuid, value text, numvalue numeric, datevalue timestamp with time zone, status boolean);

insert into pg_indexonlyscan_small
(hash, id, value, numvalue, datevalue, status)
select
	(random() * 1000000)::bigint as hash,
    gen_random_uuid() as id,
    md5(random()::text) as "value",
    10000 * random() as numvalue,
    timestamp '2001-01-01 00:00:00' + random() * (timestamp '2020-01-01 00:00:00' - timestamp '2001-01-01 00:00:00') as datevalue,
	true as status
from generate_series(1, 1000000) s(i),
pg_class c
where c.relname = 'pg_indexonlyscan_small' and c.reltuples < 100;

create index if not exists pg_indexonlyscan_small_hash on pg_indexonlyscan_small (hash, status, id);

vacuum freeze pg_indexonlyscan_small;