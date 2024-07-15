package bench.v2;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerState {

	public long iterationsDone;
	public long iterationLimit;
	public CyclicBarrier startPoint;
	public AtomicBoolean stop = new AtomicBoolean(false);
}
