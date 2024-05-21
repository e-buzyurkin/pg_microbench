package tests.basic;

import static bench.V2.*;

public class DelayImpact {

	public static void call(String query, int delay) throws InterruptedException {
		long st = 0;
		long t = 0;
		
		if (delay <= 0 ) {
			for (int i = 0; i < 10; i++) {
				st = System.nanoTime();
				sql(query);
				t += System.nanoTime() - st;
			}
		} else {
			for (int i = 0; i < 10; i++) {
				st = System.nanoTime();
				sql(query);
				t += System.nanoTime() - st;
				Thread.sleep(delay);
			}
		}
		
		System.out.println(" " + delay + "ms: avg = " + (t / (10 * 1000)) + "us");
		
	}
	
	public static void main(String[] args) {
		args(args);
		
		String query = "SELECT COUNT(DISTINCT (s_i_id))\n"
				+ "  FROM order_line, stock, district\n"
				+ " WHERE ol_w_id = 1117\n"
				+ "   AND ol_d_id = 4\n"
				+ "   AND d_w_id=1117\n"
				+ "   AND d_id=4\n"
				+ "   AND (ol_o_id < d_next_o_id)\n"
				+ "   AND ol_o_id >= (d_next_o_id - 20)\n"
				+ "   AND s_w_id = 1117\n"
				+ "   AND s_i_id = ol_i_id\n"
				+ "   AND s_quantity < 15";
		
		parallel((state) -> {
			try {
				call(query, 0);
				call(query, 0);
				call(query, 1);
				call(query, 10);
				call(query, 100);
				call(query, 1000);
				call(query, 0);
				call(query, 1);
				call(query, 10);
				call(query, 100);
				call(query, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
}
