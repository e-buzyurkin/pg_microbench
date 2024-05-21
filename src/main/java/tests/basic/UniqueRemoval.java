package tests.basic;

import static bench.V2.*;


/**
 * JMeter: 
 *  - milliseconds
 *  - GC (5-20%) - ?
 * 
 * @author mizhka
 *
 */
public class UniqueRemoval {
	
	public static void main(String[] args) {
		args(args);
		
		parallel((state) -> {
			sql("explain select distinct id from users where userid = ?");
		});
		
		parallel((state) -> {
			sql("set enable_indexscan=off");
			sql("explain (analyze, buffers, verbose) select distinct id from users");
		});
	}
}
