package tests.basic;

import static bench.V2.*;

import bench.v2.Database;

public class Connect {
	
	public static void main(String[] args) {
		// We want to create connections w/o pooling
		Database.pooling = false;
		args(args);
				
		parallel((state) -> {
			sql("select 1");
		});
	}
}
