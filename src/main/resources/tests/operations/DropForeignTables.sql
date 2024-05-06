drop foreign table if exists other_world;
drop database if exists fdw_test;
drop user mapping if exists for public server postgres_fdw_test;
drop server if exists postgres_fdw_test;
drop table if exists world;