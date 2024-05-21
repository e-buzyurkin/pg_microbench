package tests.basic;

import static bench.V2.*;

import java.util.Random;

import bench.v2.Var;

public class SelectOnly {

	public static void main(String[] args) {
		args(args);
		
		autoCommit = false;
		verbosity = true;
		
		requireData("select aid from accounts limit 1", () -> {
			
			sql("DROP TABLE IF EXISTS accounts;"
				+ "DROP INDEX IF EXISTS ind;"
				+ "CREATE TABLE accounts (aid bigserial, name text, value bigint, etransfer bigint, itransfer bigint, net bigint DEFAULT 0, nit bigint DEFAULT 0);"
				+ "CREATE INDEX ind ON accounts (aid);");
			

			parallel((state) -> {
				Random r = new Random();
				sql("insert into accounts (name, value, etransfer, itransfer, net, nit)\n"
						+ "SELECT 'Name' || gs.*::text," + r.nextInt(1000) + "," + r.nextInt(1000) + ","
						+ r.nextInt(1000) + "," + r.nextInt(1000) + "," + r.nextInt(1000)
						+ " FROM generate_series(1,10000) AS gs");
			});
			return null;
		});
		
		
		Var aid = var("select min(aid), max(aid) from accounts", RangeOption.RANDOM);
		
		autoCommit = true;
		parallel((state) -> {
			sql("SELECT value FROM accounts WHERE aid = ?", aid.get());
		});
	}
}
