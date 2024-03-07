package tests;

import static bench.V2.*;

import java.util.Arrays;
import java.util.Random;

public class SdmSelect {

	public static void main(String[] args) {
		args(args);

		verbosity = true;
		autoCommit = false;
		
		requireData("select 1 from accounts limit 1", () -> {
			psql("SdmSimpleTable.sql", 0);

			parallel((state) -> {
				Random r = new Random();
				sql("insert into accounts (name, value, etransfer, itransfer, net, nit)\n"
						+ "SELECT 'Name' || gs.*::text," + r.nextInt(1000) + "," + r.nextInt(1000) + ","
						+ r.nextInt(1000) + "," + r.nextInt(1000) + "," + r.nextInt(1000)
						+ " FROM generate_series(1,10000) AS gs");
				commit();
			});
			return null;
		});

		Random r = new Random();
		sessionAffinity = false;
		
		parallel((state) -> {
			Integer key = r.nextInt(10000 * params.volume);
			ctx("accounts", Arrays.asList(key), state, (st) -> {
				sql("SELECT name, value, etransfer, itransfer, net, nit FROM accounts WHERE aid = ?", key);
				commit();
			});
		});
	}
}
