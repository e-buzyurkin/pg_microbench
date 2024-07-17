package bench;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.HdrHistogram.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bench.v2.Configuration;
import bench.v2.Configuration.Phase;
import bench.v2.DataContext;
import bench.v2.Database;
import bench.v2.Database.CallableStatement;
import bench.v2.PSQL;
import bench.v2.Results;
import bench.v2.Snap;
import bench.v2.WorkerState;
import bench.v2.WorkerUnit;
import bench.v2.strategy.StrategyType;

public class V2 {
	
	static {
		System.setProperty("logback.configurationFile", "logback.xml");
		System.setProperty("org.slf4j.simpleLogger.showDateTime","true");
		System.setProperty("org.slf4j.simpleLogger.dateTimeFormat","yyyy-MM-dd HH:mm:ss:SSS Z");
	}
	
	private final static String lineSep = "-------------------------------------------------------------------";
	
	public static final Logger log = LoggerFactory.getLogger(V2.class);
	
	private static final String DEFPGPORT="5432";
	private static final String DEFTIMEOUT="10";
	private static final String DEFWORKERS="5";
	private static final String DEFCONCURRENCY="10";
	private static final String DEFVOLUME="10";
	private static final String DEFRUNTYPE=Phase.EXECUTE.toString();
	private static final String DEFSTRATEGY="none";
	private static final int fetchSize = 1000;
	private static ScheduledExecutorService pool;

	private static AtomicHistogram histogram = 
							new AtomicHistogram(10000000, 
						3);
	
	public enum RangeOption{
		RANDOM,
		SHARED
	}
	
	public static Database db;
	public static Configuration params;
	
	public static Boolean verbosity = false;
	public static Boolean sessionAffinity = true;
	public static Boolean autoCommit = true;
	
	private static AtomicBoolean dbGen = new AtomicBoolean(false);

	private static void printHistogramInFile(PrintStream stream) {
		RecordedValuesIterator iterator = new RecordedValuesIterator(histogram);

		iterator.reset();
		while (iterator.hasNext()) {
			HistogramIterationValue val = iterator.next();
			stream.println(val.getValueIteratedTo());
		}
	}

	public static void sql(String sql, Object... binds) {
		db.<Boolean>execute((conn) -> {
			try(PreparedStatement pstmt = conn.prepareStatement(sql);) {

				for(int i = 0; i < binds.length; i++) {
					pstmt.setObject(i + 1, binds[i]);
				}

				pstmt.setFetchSize(fetchSize);
				return pstmt.execute();
			}
		});
	}
	
	public static void sqlNoPrepare(String sql) {
		db.<Boolean>execute((conn) -> {
			try(Statement stmt = conn.createStatement();)
			{
				return stmt.execute(sql);
			}
		});
	}
	
	public static <V> V sqlCustom(CallableStatement<V> custom) {
		return db.execute(custom);
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E selectOne(String sql, Object... binds) {
		List<E> x = new ArrayList<>();
		db.selectSingle((rs) -> {
			x.add((E) rs.getObject(1));
		}, sql, binds);
		return x.isEmpty() ? null : x.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static <E> List<E> select(String sql, Object... binds) {
		return db.list((rs) -> {return (E) rs.getObject(1);}, sql, binds);
	}
	
	public static List<String> explainResults(String sql, Object... binds) {
		return select("explain (analyze, verbose, buffers, costs off) " + sql, binds);
	}

	public static void explain(Logger log2, String sql, Object... binds) {
		List<String> lines = select("explain (analyze, verbose, buffers) " + sql, binds);
		if (log != null)
			log.info("Actual plan \n{}\n{}\n{}", lineSep, String.join("\n", lines), lineSep);
	}
	
	private static void preinit() {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			log.error("Test stopped due to error:", unwrap(e));
		});
	}
	
	public static void args(String[] args) {
		preinit();
		
		Options opt = new Options();	
		
		/* Database options */
		opt.addOption(Option.builder("h").hasArg().argName("host").required()
				.desc("database host name").build());
		
		opt.addOption(Option.builder("p").hasArg().argName("port")
				.desc("database port. Defaults to " + DEFPGPORT).build());
		
		opt.addOption(Option.builder("d").hasArg().argName("database")
				.desc("database name. Defaults to 'postgres'").build());
		
		opt.addOption(Option.builder("U").hasArg().argName("username")
				.desc("user name. Defaults to 'postgres'").build());

		opt.addOption(Option.builder("P").hasArg().argName("password")
				.desc("user password").build());

		/* Workload options */
		opt.addOption(Option.builder("w").hasArg().argName("workers")
				.desc("amount of workers. Defaults to " + DEFWORKERS)
				.build());
		opt.addOption(Option.builder("c").hasArg().argName("concurrency")
				.desc("amount of concurrent workers. Defaults to " + DEFCONCURRENCY)
				.build());
		opt.addOption(Option.builder("o").hasArg().argName("run type")
				.desc("Run type (generate,run). Defaults to " + DEFRUNTYPE)
				.build());
		opt.addOption(Option.builder("v").hasArg().argName("volume")
				.desc("Volume size. Defaults to " + DEFVOLUME)
				.build());

		opt.addOption(Option.builder("t").hasArg().argName("timeout")
				.desc("test duration. Default to " + DEFTIMEOUT)
				.build());
		opt.addOption(Option.builder("s").hasArg().argName("timeout")
				.desc("Warker distribute strategy. Default to " + DEFSTRATEGY)
				.build());
		opt.addOption(Option.builder("T").hasArg().argName("txLimit")
				.desc("max amount of transactions. Disabled by default")
				.build());
		//must be over 30
		opt.addOption(Option.builder("l").hasArg().argName("cnTimeLimit")
				.desc("max life time of connection in seconds. Disabled by default")
				.build());
		opt.addOption("profiling", false, "enable profiling in benchmark");

		params = new Configuration();
		try {
			CommandLine cmd = new DefaultParser().parse(opt, args);
						
			params.workers = Integer.parseInt(cmd.getOptionValue("w", DEFWORKERS));
			params.concurrency = Integer.parseInt(cmd.getOptionValue("c", DEFCONCURRENCY));
			params.strategy = StrategyType.valueOf(cmd.getOptionValue("s",DEFSTRATEGY).toUpperCase());
			params.volume = Integer.parseInt(cmd.getOptionValue("v", DEFVOLUME));
			params.runType = Phase.valueOf(cmd.getOptionValue("o", DEFRUNTYPE));
			
			params.timeout = Integer.parseInt(cmd.getOptionValue("t", DEFTIMEOUT));
			params.txlimit = new AtomicLong(Long.parseLong(cmd.getOptionValue("T", "-1")));
            params.isProfiling = cmd.hasOption("profiling");
			
			db = new Database(
					cmd.getOptionValue("h"), 
					Integer.parseInt(cmd.getOptionValue("p",DEFPGPORT)),
					cmd.getOptionValue("d","postgres"),
					cmd.getOptionValue("U","postgres"),
					cmd.getOptionValue("P","postgres"),
					params.workers,
					Long.parseLong(cmd.getOptionValue("l", "0")) * 1000L,
			true
			);
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -Xmx256m -jar pg_selectonly.jar", opt, true);
			System.out.println();
			/* Print exception at end */
			System.out.println("\033[0;1m[ERROR]: " + e.getMessage() + "\033[0m");
			System.exit(-1);
		}
	}
	
	public static void begin() {
		try{
			db.get().setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException("Error on setAutoCommit(false)", e);
		}
	}

	public static void commit() {
		try {
			db.get().commit();
		} catch (SQLException e) {
			throw new RuntimeException("Error on commit", e);
		}
	}
	
	public static void ctx(String tableName, 
						   List<Object> keyValues, 
						   WorkerState workerState, 
						   final WorkerUnit workerUnit) 
	{
		if (db.get() != null) {
			throw new RuntimeException("Connection is already defined");
		}
		try (Connection cc = db.getDataSource(new DataContext(tableName, keyValues)).getConnection()){
			db.push(cc);
			workerUnit.iterate(workerState);
			db.pop();
		} catch (SQLException e) {
			throw new RuntimeException("Error on commit", e);
		}
	}
	
	public static void transaction(String tableName, 
                                   List<Object> keyValues, 
                                   WorkerState workerState, 
                                   final WorkerUnit workerUnit) 
	{	
		Connection c = db.get();
		try {
			if (c != null) {
				c.setAutoCommit(false);
				workerUnit.iterate(workerState);
				c.commit();
			} else {
				try (Connection cc = db.getDataSource(new DataContext(tableName, keyValues)).getConnection()) {
					db.push(cc);
					cc.setAutoCommit(false);
					workerUnit.iterate(workerState);
					cc.commit();
					db.pop();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error on commit", e);
		}
	}
	
	public static void transaction(WorkerState workerState, 
								   final WorkerUnit workerUnit) 
	{
		Connection c = db.get();
		try {
			if (c != null) {
				c.setAutoCommit(false);
				workerUnit.iterate(workerState);
				c.commit();
			} else {
				try (Connection cc = db.getDataSource().getConnection()) {
					db.push(cc);
					cc.setAutoCommit(false);
					workerUnit.iterate(workerState);
					cc.commit();
					db.pop();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error on commit", e);
		}
	}
	
	public static Results parallel(final WorkerUnit workerUnit) {
		List<Snap> metrics = new ArrayList<>(1000);
		int period = 100;
		long durNs;
		String logResultsIntro;
		
		switch (params.runType) {
			case GENERATE:
				if (!dbGen.get()) {
					return null;
				}
				log.info("Starting {} workers for generate {} rows", params.workers, params.volume);
				durNs = parallelInternal(workerUnit, params.volume, 
											Integer.MAX_VALUE, metrics, 
											period, false);
				logResultsIntro = "Generation completed after";
				break;

			case EXECUTE:
				log.info("Starting {} workers for {} seconds", params.workers, params.timeout);
				durNs = parallelInternal(workerUnit, 0, 
										params.timeout, metrics, 
										period, verbosity);
				logResultsIntro = "Test completed after";
				break;
				
			default:
				return null;
		}
		
		if (metrics.size() > 0) {
			Results r = new Results(metrics, period, durNs);
			r.logSummary(log, logResultsIntro);

			try {
				histogram.outputPercentileDistribution(new PrintStream(new File("histogram.hdr")), 5, 1.0);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				printHistogramInFile(new PrintStream(new File("histo.txt")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			return r;
		} else return null;
	}
	
	private static long parallelInternal(final WorkerUnit x, 
										 long iterationLimit, 
										 int timeout, 
										 List<Snap> snaps, 
										 int period, 
										 boolean monVerbose) 
	{
		pool = Executors.newScheduledThreadPool(params.workers);
		ScheduledExecutorService mon = Executors.newScheduledThreadPool(1);

		CyclicBarrier c = new CyclicBarrier(params.workers + 1);

		Integer mainLimit = 0;
		Integer extraUnits = 0;
		
		if (iterationLimit > 0) {
			mainLimit = params.volume / params.workers;
			extraUnits = params.volume % params.workers;
		}
		
		db.getDataSource();
		
		List<WorkerState> states = new ArrayList<WorkerState>();
		for (int i = 0; i < params.workers; i++) {
			WorkerState st = new WorkerState();	
			
			st.iterationsDone = 0;
			st.startPoint = c;
			
			st.iterationLimit = mainLimit;
			st.iterationLimit += (i < extraUnits) ? 1 : 0;
			
			states.add(st);
			
			pool.schedule(() -> {
				try {
					try (Connection conn = db.getDataSource().getConnection()) {
						st.startPoint.await();
					}
					
					do {
						if (iterationLimit > 0 && st.iterationsDone >= st.iterationLimit)
							break;
						if (sessionAffinity) {
							try (Connection conn = db.getDataSource().getConnection()) {
								db.push(conn);
								x.iterate(st);
							} finally {
								db.pop();
							}
						} else {
							x.iterate(st);
						}
						
						st.iterationsDone++;
					} while (!st.stop.get());
				} catch (BrokenBarrierException e) {
					return null;
				} catch (Throwable e) {
					log.error("Occuried error", e);
					throw e;
				}
				
				return null;
			}, 0, TimeUnit.SECONDS);
		}
		
		log.info("Waiting workers' readiness");
		
		try {
			/* Let's wait for all threads to be ready */
			c.await();
		} catch (BrokenBarrierException | InterruptedException e) {
			log.error("Occuried error", e);
			return -1;
		}
		
		Long startTime = System.nanoTime();
		if (snaps != null) {
			AtomicLong curIter = new AtomicLong(0);
			
			Long start = System.nanoTime();
			Long initialDelay = 1000 - (System.currentTimeMillis() % 1000);
			mon.scheduleAtFixedRate(() -> {
				long iterations = 0;
				
				Long n = System.nanoTime();
				for (WorkerState st : states) {
					iterations += st.iterationsDone;
				}
				curIter.set(iterations);
				
				Snap p = new Snap();
				p.ts = n - start;
				p.iterations = iterations;
				
				Long tps = iterations * 1000000000 / p.ts;
				histogram.recordValue(tps);

				snaps.add(p);
			}, initialDelay, period, TimeUnit.MILLISECONDS);
		}
		
		pool.shutdown();
		try {
			pool.awaitTermination(timeout, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		Long endTime = System.nanoTime();
		
		if (snaps != null) {
			mon.shutdown();
		}
		
		for (WorkerState st : states) {
			st.stop.set(true);
		}
		
		return endTime - startTime;
	}

	
	public static Throwable unwrap(Throwable throwable) {
		Objects.requireNonNull(throwable);
		Throwable rootCause = throwable;
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
			rootCause = rootCause.getCause();
		}
		return rootCause;
	}
	
	private static String getDatabaseSettingValue(String gucName) {
		//TODO: fetch value from pg_settings
		return "";
	}
	
	public static void requireSettings(String gucName, Comparable<String> comparator) {
		String value = getDatabaseSettingValue(gucName);
		if (comparator.compareTo(value) == 0) {
			return;
		}
		
		//TODO: set value
	}
	
	public static void psql(String filename, Integer hostNum) {
		try (Connection conn = db.getDataSource().getConnection()) {
			log.info("Execute SQL script {} via psql host {}", filename, db.hosts[hostNum]);
			PSQL.executeFile(filename, hostNum);
			log.info("Completed SQL script");
		}
		catch (Exception e)
		{
			log.error("Occuried error", e);
		}
	}
	
	public static void requireData(String checkSQL, String filename) {
		requireData(checkSQL, filename, 0);
	}
	
	public static void requireData(String checkSQL, String filename, Integer hostNum) {
		Callable<Void> psql = () -> {
			log.info("Execute SQL script {} via psql host {}", filename, db.hosts[hostNum]);
			psql(filename, hostNum);
			log.info("Completed SQL script");
			return null;
		};
		
		requireData(checkSQL, psql);
	}
	
	public static void requireData(String checkSQL, 
								   String filename, 
								   String username, 
								   String password, 
								   String database, 
								   Integer hostNum) 
	{
		Callable<Void> psql = () -> {
			log.info("Execute SQL script {} via psql host {} under user {} in database {}", 
											filename, db.hosts[hostNum], username, database);
			PSQL.executeFile(filename, username, password, database, hostNum);
			log.info("Completed SQL script");
			return null;
		};
		
		requireData(checkSQL, psql);
	}
	
	public static void requireData(String checkSQL, Callable<Void> psql) {
		// check datasource
		try {
			db.getDataSource();
		} catch (Throwable e) {
			log.error("Some error", e);
			try{
				dbGen.set(true);
				log.info("Can't get DS, safe mode...");
				psql.call();
			} catch (Exception x) {
				log.error("Some error", e);
				throw new RuntimeException("Exception occured during error handling...", x);
			} finally {
				dbGen.set(false);
			}
		}
		
		CallableStatement<Boolean> checkStmt = (conn) -> {
			try(PreparedStatement pstmt = conn.prepareStatement(checkSQL);) {
				pstmt.setFetchSize(fetchSize);
				return pstmt.execute();
			}
		};
		
		Callable<Void> handleOnError = () -> {
			try{
				dbGen.set(true);
				log.info("Handle SQL error, safe mode...");
				psql.call();
			} finally {
				dbGen.set(false);
			}
			
			return null;
		};
		
		db.<Boolean>execute(checkStmt, handleOnError);
	}
	
	public static void assertSimilar(Results a, 
									 Results b, 
									 String msgFormat, 
									 Object... params) 
	{
		if (!a.similar(b)) {
			log.error("TEST FAILED: " + msgFormat, params);
		} else {
			log.info("TEST PASSED: " + msgFormat, params);
		}
	}
	
	public static void logResults(Results res) {
		log.info("Test results: last 5 sec {} tps, overall {} tps, {} iterations", 
										res.tpsLast5sec, res.tps, res.iterations);
	}

	public static void closeConnection() {
		if (db == null) {
			return;
		}

		try {
			if (pool != null) {
				pool.shutdown();
				pool.awaitTermination(10, TimeUnit.SECONDS);
			}

			db.close();
			log.info("Database connection closed successfully.");
		} catch (SQLException e) {
			log.error("Failed to close database connection.", e);
		} catch (InterruptedException e) {
			log.error("Thread was interrupted while waiting for thread pool termination.", e);
		}
		
	}
}
