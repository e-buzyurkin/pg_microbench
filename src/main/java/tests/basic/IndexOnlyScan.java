package tests.basic;

import static bench.V2.*;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bench.v2.Results;
import bench.v2.Var;


/** 
 * The main purpose of test is to check bulk (100 items) IndexOnlyScan node
 * 
 * Actual plan:
 *                                                QUERY PLAN
 * -----------------------------------------------------------------------------------------------------------
 *  Aggregate
 *    ->  Limit
 *          ->  Index Only Scan using pg_indexonlyscan_hash on pg_indexonlyscan
 *                Index Cond: ((hash >= '200000'::bigint) AND (hash <= '400000'::bigint) AND (status = true))
 * (4 rows)
 * 
 * For reference:
 * 
 * prepare q(bigint,bigint,bigint) as select hash from pg_indexonlyscan where hash between $1 and $2 and status limit $3;
 * 
 * explain (costs off) execute q(200000,400000,100);
 * 
 * @author mizhka
 *
 */
public class IndexOnlyScan {
	private static final Logger log = LoggerFactory.getLogger(IndexOnlyScan.class);

	public static Results test(String tableName, Long chunkSize) {
		Var hash = Var.var("select min(hash), max(hash) from "+tableName, RangeOption.SHARED);
		
		String query = "select hash, id from " + tableName + " where hash between ? and ? and status order by hash limit ?";
		
		Results res = parallel((state) -> {
			List<Long> hashes = select(query, hash.get(), hash.max(), chunkSize);
			
			Long max = hashes.stream().collect(Collectors.maxBy(Long::compareTo)).get();
			
			for (Long x : hashes) {
				if (x > max) {
					max = x;
				}
			}
			
			if (max == hash.max())
				hash.set(hash.min());
			else
				hash.set(max);
		});
		
		for (int i = 0; i < 10; i++)
			explain(null, query, hash.get(), hash.max(), chunkSize);
		
		explain(log, query, hash.get(), hash.max(), chunkSize);
		
		return res;
	}
	
	
	public static void main(String[] args) {
		args(args);
		
		requireData("select 1 from pg_indexonlyscan limit 1", "IndexOnlyScan.sql", "postgres", "postgres", "postgres",0);
		requireData("select 1 from pg_indexonlyscan_small limit 1", "IndexOnlyScan.sql", "postgres", "postgres", "postgres",0);
		
		final Long chunkSize = 100l;
		Results big = test("pg_indexonlyscan", chunkSize);
		Results small = test("pg_indexonlyscan_small", chunkSize);
		
		assertSimilar(big, small, "Test results: small table = {} tps, big table = {} tps", small.tpsLast5sec, big.tpsLast5sec);
	}
}