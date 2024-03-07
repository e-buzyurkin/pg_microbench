package tests;

import static bench.V2.args;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static bench.V2.*;

public class PgProStatsPreparedPlans {
	public static void main(String[] args) {
		args(args);
		
		String sql = "SELECT c_first FROM customer WHERE c_w_id = ? AND c_d_id = ? ORDER BY c_first limit 1";
		
		String x = sqlCustom((conn) -> {
			
			conn.setAutoCommit(false);
			
			Statement resetStats = conn.createStatement();
			resetStats.execute("select pgpro_stats_statements_reset()");
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setObject(1, 15);
			stmt.setObject(2, 1);
			
			int cnt = stmt.getMetaData().getColumnCount();
			
			log.info("Counts: {}", cnt);
			
			Statement startTrans = conn.createStatement();
			
			startTrans.execute("BEGIN READ WRITE");
			
			stmt.execute();
			
			conn.commit();
			
			Statement queryPlan = conn.createStatement();
			queryPlan.execute("select plan, query from pgpro_stats_statements where query like '%customer%'");
			ResultSet rs = queryPlan.getResultSet();
			
			if (rs.next()) {
				log.info("Fetched plan: {}", rs.getString(1));
				log.info("Fetched query: {}", rs.getString(2));
			}
			
			rs.close();
			queryPlan.close();
			
			resetStats.close();
			stmt.close();
			startTrans.close();
			conn.close();
			
			return "";
		});
		
		//String x = selectOne("SELECT c_first FROM customer WHERE c_w_id = ? AND c_d_id = ? ORDER BY c_first limit 1", 15, 1);
		log.info("Output: {}",x);
	}
}
