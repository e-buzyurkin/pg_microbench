package tests.basic;

import static bench.V2.*;

public class LostConnection {

	public static void main(String[] args) {
		args(args);
				
		parallel((state) -> {
			Integer pid1 = selectOne("select pg_backend_pid()");
			Integer pid2 = selectOne("select pg_backend_pid()");
			if (!pid1.equals(pid2)) {
				throw new RuntimeException("Connection has been changed with pids = " + pid1 + " " + pid2);
			}
		});
	}
}
