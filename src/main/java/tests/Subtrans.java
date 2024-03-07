package tests;

import static bench.V2.*;

import bench.v2.Var;

public class Subtrans {
	
	public static void main(String[] args) {
		args(args);
		
		requireData("select 1 from contend limit 1", "Subtrans.sql",0);
		
		Var rnd = var("select min(id), max(id) from contend", RangeOption.RANDOM);
		
		parallel((state)-> {
			begin();
			Long id = rnd.get();
			for (int i = 0; i < 100; i++) {
				sql("select count(*) from contend");
				sql("update contend set val = val + 1 where id in (?)", id + i);
				sql("savepoint a");
			}
			commit();
		});
	}
}
