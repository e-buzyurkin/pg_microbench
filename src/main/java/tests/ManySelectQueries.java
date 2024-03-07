package tests;

import static bench.V2.*;

import bench.v2.Var;

/* 10000 tables */
public class ManySelectQueries {

	public static void main(String[] args) {
		args(args);
		
		requireData("select 1 from t_tab_10000 limit 1", "ManySelectQueries.sql",0);
		
		Var tableID = var(1l,10000l,RangeOption.RANDOM);
		Var c3 = var(0l,99l,RangeOption.RANDOM);
		Var id = var(1l,1000l,RangeOption.RANDOM);
		Var c2 = var(0l,10l,RangeOption.RANDOM);
		
		parallel((state) -> {
			sql("select max(c1) from t_tab_" + tableID + " where c3 = ?", c3.get());
			sql("select id from t_tab_" + tableID + " where c3 = ? order by c1 limit 1", c3.get());
			sql("select c2 from t_tab_" + tableID + " where id = ?", id.get());
			sql("select id from t_tab_" + tableID + " where c2 = ? and c3 = ?", c2.get(), c3.get());
			sql("select max(c2), min(c2) from t_tab_" + tableID);
		});
	}
}
