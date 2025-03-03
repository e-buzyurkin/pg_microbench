package bench.v2;

import java.util.concurrent.atomic.AtomicLong;

import bench.v2.strategy.StrategyType;

public class Configuration {

    public enum Phase {
        GENERATE,
        EXECUTE
    }

    /* Worker parameters */
    public Integer startPoint;
    public Integer workers;
    public Integer concurrency;
    public Integer loops;
    public Integer timeout;
    public AtomicLong txlimit;
    public boolean isProfiling;

    public Integer volume;
    public Phase runType;
    public StrategyType strategy;
}
