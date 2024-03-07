package tests;

import static bench.V2.*;

import java.util.Random;

public class SdmInsert {

	public static void main(String[] args) {
		args(args);

		requireData("select 1 from accounts limit 1", () -> {
			psql("SdmSimpleTable.sql", 0);

			parallel((state) -> {
				Random r = new Random();
				sql("insert into accounts (name, value, etransfer, itransfer, net, nit)\n"
						+ "SELECT 'Name' || gs.*::text," + r.nextInt(1000) + "," + r.nextInt(1000) + ","
						+ r.nextInt(1000) + "," + r.nextInt(1000) + "," + r.nextInt(1000)
						+ " FROM generate_series(1,10000) AS gs");
			});
			return null;
		});

		parallel((state) -> {
			Random r = new Random();
			sql("insert into accounts (name, value, etransfer, itransfer, net, nit)\n" + "SELECT 'Name',"
					+ r.nextInt(1000) + "," + r.nextInt(1000) + "," + r.nextInt(1000) + "," + r.nextInt(1000) + ","
					+ r.nextInt(1000));
		});
	}
}
