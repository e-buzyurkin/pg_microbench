package tests.basic;

import static bench.V2.*;

import java.util.Random;

public class Sdm1865 {

	public static void main(String[] args) {
		args(args);

		verbosity = true;
		parallel((state) -> {
			Random r = new Random();

			Integer bid = r.nextInt(params.volume);
			Integer aid = r.nextInt(100000) + 100000 * (bid - 1);

			sql("select abalance from pgbench_accounts where bid = " + bid + " and aid = " + aid);
		});	
	}
}
