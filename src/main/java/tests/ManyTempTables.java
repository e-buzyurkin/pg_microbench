package tests;

import bench.v2.Var;

import static bench.V2.*;

public class ManyTempTables {

    public static void main(String[] args) {
        args(args);

        requireData("select 1 from tt1", "ManyTempTables.sql",0);

        String callProcedure = "CALL create_tmp();\n";
        Var tableID = var(1L, 40L,RangeOption.RANDOM);

        parallel((state) -> {
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$ \n" +
                        "begin\n" +
                        "    drop table if exists tt" + strTbl + " cascade;\n" +
                        "    create temporary table tt" + strTbl + " (\n" +
                        "        id bigserial primary key,\n" +
                        "        val text,\n" +
                        "        c1 text,\n" +
                        "        c2 int4,\n" +
                        "        c3 int4\n" +
                        ") without oids;\n" +
                        "    commit;\n" +
                        "END$$;\n");
            }
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$ \n" +
                        "begin\n" +
                        "    drop table if exists tt" + strTbl + " cascade;\n" +
                        "    create temporary table tt" + strTbl + " (\n" +
                        "        id bigserial primary key,\n" +
                        "        val text,\n" +
                        "        c1 text,\n" +
                        "        c2 int4,\n" +
                        "        c3 int4\n" +
                        ");\n" +
                        "    commit;\n" +
                        "END$$;\n");
            }
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$ \n" +
                        "begin\n" +
                        "    drop table if exists tt" + strTbl + " cascade;\n" +
                        "    create temporary table tt" + strTbl + " (\n" +
                        "        id bigserial primary key,\n" +
                        "        val text,\n" +
                        "        c1 text,\n" +
                        "        c2 int4,\n" +
                        "        c3 int4\n" +
                        ") without oids;\n" +
                        "    rollback;\n" +
                        "END$$;\n");
            }
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$ \n" +
                        "begin\n" +
                        "    drop table if exists tt" + strTbl + " cascade;\n" +
                        "    create temporary table tt" + strTbl + " (\n" +
                        "        id bigserial primary key,\n" +
                        "        val text,\n" +
                        "        c1 text,\n" +
                        "        c2 int4,\n" +
                        "        c3 int4\n" +
                        ") without oids on commit drop;\n" +
                        "    rollback;\n" +
                        "END$$;\n");
            }
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$ \n" +
                        "begin\n" +
                        "    drop table if exists tt" + strTbl + " cascade;\n" +
                        "    create temporary table tt" + strTbl + " (\n" +
                        "        id bigserial primary key,\n" +
                        "        val text,\n" +
                        "        c1 text,\n" +
                        "        c2 int4,\n" +
                        "        c3 int4\n" +
                        ") without oids on commit PRESERVE ROWS;\n" +
                        "END$$;\n");
            }
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$ \n" +
                        "begin\n" +
                        "    drop table if exists tt" + strTbl + " cascade;\n" +
                        "    create temporary table tt" + strTbl + " (\n" +
                        "        id bigserial primary key,\n" +
                        "        val text,\n" +
                        "        c1 text,\n" +
                        "        c2 int4,\n" +
                        "        c3 int4\n" +
                        ") without oids on commit DELETE ROWS;\n" +
                        "END$$;\n");
            }
            sql(callProcedure +"ANALYZE pg_temp.tt" + tableID + ";");
            sql(callProcedure + "insert into tt"+ tableID + "(val, c1, c2, c3)\n" +
                    "select repeat('x', 50)::text as val," +
                    "   md5(id::text || 'name') as c1," +
                    "   id / 100 as c2, mod(id, 100) as c3 " +
                    "from generate_series(1, 10000) id;");
            sql(callProcedure + "insert into tt"+ tableID + "(val, c1, c2, c3)\n" +
                    "select repeat('x', 50)::text as val," +
                    "   md5(id::text || 'name') as c1," +
                    "   id / 100 as c2, mod(id, 100) as c3 " +
                    "from generate_series(1, 1000) id;");
            sql(callProcedure + "insert into tt"+ tableID + "(val, c1, c2, c3)\n" +
                    "select repeat('x', 50)::text as val," +
                    "   md5(id::text || 'name') as c1," +
                    "   id / 100 as c2, mod(id, 100) as c3 " +
                    "from generate_series(1, 100) id;");
            sql(callProcedure + "insert into tt"+ tableID + "(val, c1, c2, c3)\n" +
                    "select repeat('x', 50)::text as val," +
                    "   md5(id::text || 'name') as c1," +
                    "   id / 100 as c2, mod(id, 100) as c3 " +
                    "from generate_series(1, 10) id;");
            sql(callProcedure +
                    "DO $$\n" +
                    "begin\n" +
                        "insert into tt"+ tableID + "(val, c1, c2, c3)\n" +
                        "select repeat('x', 50)::text as val," +
                        "   md5(id::text || 'name') as c1," +
                        "   id / 100 as c2, mod(id, 100) as c3 " +
                        "from generate_series(1, 1000) id;\n" +
                        "rollback;\n" +
                        "END$$;\n");
            sql(callProcedure +
                    "DO $$\n" +
                    "begin\n" +
                    "insert into tt"+ tableID + "(val, c1, c2, c3)\n" +
                    "select repeat('x', 50)::text as val," +
                    "   md5(id::text || 'name') as c1," +
                    "   id / 100 as c2, mod(id, 100) as c3 " +
                    "from generate_series(1, 100) id;\n" +
                    "rollback;\n" +
                    "END$$;\n");

            sql(callProcedure + "create index on tt"+ tableID + " (val);");
            sql(callProcedure + "drop index if exists tt" + tableID + "_val_idx;");
            sql(callProcedure + "drop index if exists tt" + tableID + "_val_idx1;");
            sql(callProcedure + "drop index if exists tt" + tableID + "_val_idx2;");
            sql(callProcedure + "select max(c2), min(c2) from tt" + tableID + ";");
            sql(callProcedure +
                    "DO $$\n" +
                    "begin\n" +
                    "create index on tt"+ tableID + " (val);\n" +
                    "rollback;\n" +
                    "END$$;\n");
            sql(callProcedure +
                    "DO $$\n" +
                    "begin\n" +
                    "drop index if exists tt" + tableID + "_val_idx;" +
                    "rollback;\n" +
                    "END$$;\n");
            {
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$\n" +
                        "begin\n" +
                        "    if not exists (\n" +
                        "      select 1\n" +
                        "        from pg_constraint\n" +
                        "        where conname = 'tt" + strTbl + "_id_fkey'\n" +
                        "          and connamespace = pg_my_temp_schema()\n" +
                        "    ) then\n" +
                        "        alter table tt" + strTbl + "\n" +
                        "            add constraint tt" + strTbl + "_id_fkey\n" +
                        "            foreign key(id) references tt41(id);\n" +
                        "    end if;\n" +
                        "end;\n" +
                        "$$;"
                );
            }{
                String strTbl = tableID.toString();
                sql(callProcedure +
                        "DO $$\n" +
                        "begin\n" +
                        "    if exists (\n" +
                        "      select 1\n" +
                        "        from pg_constraint\n" +
                        "        where conname = 'tt" + strTbl + "_id_fkey'\n" +
                        "          and connamespace = pg_my_temp_schema()\n" +
                        "    ) then\n" +
                        "        alter table tt" + strTbl + "\n" +
                        "            DROP CONSTRAINT tt" + strTbl + "_id_fkey;\n" +
                        "    end if;\n" +
                        "end;\n" +
                        "$$;"
                );
            }
        });
    }
}
