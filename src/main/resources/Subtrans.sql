CREATE UNLOGGED TABLE contend (
    id integer PRIMARY KEY,
    val integer NOT NULL
)
WITH (fillfactor='50');

INSERT INTO contend (id, val)
SELECT i, 0
FROM generate_series(1, 10000) AS i;
 
VACUUM (ANALYZE) contend;