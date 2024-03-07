DROP TABLE IF EXISTS accounts;
DROP INDEX IF EXISTS ind;
CREATE TABLE accounts (aid bigserial, name text, value bigint, etransfer bigint, itransfer bigint, net bigint DEFAULT 0, nit bigint DEFAULT 0) WITH (distributed_by='aid', num_parts=18);
CREATE INDEX ind ON accounts (aid);