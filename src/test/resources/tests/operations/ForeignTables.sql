create table if not exists world (
    greeting TEXT
);

create extension if not exists postgres_fdw;

create server if not exists postgres_fdw_test
foreign data wrapper postgres_fdw
options (host 'localhost', port '5433', dbname 'postgres');

create user mapping if not exists for public server postgres_fdw_test
options (password '');

create foreign table other_world (greeting TEXT)
server postgres_fdw_test
options (table_name 'world');

