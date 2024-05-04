create extension postgres_fdw;
create server postgres_fdw_test
foreign data wrapper postgres_fdw
options (host 'localhost', dbname 'fdw_test');

create user mapping for public server postgres_fdw_test
options (password '');

create foreign table other_world (greeting TEXT)
server postgres_fdw_test
options (table_name 'world');