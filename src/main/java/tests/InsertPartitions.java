package tests;

import static bench.V2.*;

import bench.v2.RandomList;

public class InsertPartitions {
	
	public static void main(String[] args) {
		args(args);
		
		final RandomList<String> tables = new RandomList<>("test1", "test2", "test3", "test4");
		
		parallel((state) -> {
			begin();
			String query = String.format("INSERT INTO %s (key, value, ts) "
					+ "VALUES (md5(random()::text), 0.01245, now() - random() * INTERVAL '4 months')", 
					tables.get());
			
			for (int i = 0; i < 500; i++){
				sql(query);
			}
			commit();
		});
	}
}
