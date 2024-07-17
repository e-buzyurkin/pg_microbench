package bench.v2;

import bench.V2;

import java.util.Random;

import static bench.V2.db;

public class Var {
    public Random rnd = new Random();
    public Long value;

    public Long start = 0L;
    public Long end = Long.MAX_VALUE;

    public Long get() {
        if (rnd == null)
            return value;

        if (end == Long.MAX_VALUE && start == 0L) {
            return rnd.nextLong();
        }

        return (long) (start + rnd.nextFloat() * (end - start));
    }



    /* Variables */
    public static Var var(String sql, V2.RangeOption... options) {
        Var res = new Var();
        for (V2.RangeOption option : options ) {
            switch (option) {
                case RANDOM:
                    res.rnd = new Random();
                    break;

                case SHARED:
                    //TODO: handle shared & rename "shared" to more reasonable
                    throw new UnsupportedOperationException();
            }
        }


        db.selectSingle((rs) -> {
            res.start = rs.getLong(1);
            res.end = rs.getLong(2);
            return;
        }, sql);

        return res;
    }

    public static Var var(Long min, Long max, V2.RangeOption... options) {
        Var res = new Var();
        for (V2.RangeOption option : options ) {
            switch (option) {
                case RANDOM:
                    res.rnd = new Random();
                    break;

                case SHARED:
                    //TODO: handle shared & rename "shared" to more reasonable
                    throw new UnsupportedOperationException();
            }
        }

        res.start = min;
        res.end = max;

        return res;
    }

    public static Var var(Integer min, Integer max, V2.RangeOption... options) {
        Var res = new Var();
        for (V2.RangeOption option : options ) {
            switch (option) {
                case RANDOM:
                    res.rnd = new Random();
                    break;

                case SHARED:
                    //TODO: handle shared & rename "shared" to more reasonable
                    throw new UnsupportedOperationException();
            }
        }

        res.start = min.longValue();
        res.end = max.longValue();

        return res;
    }

    public void set(Long x) {
        value = x;
    }

    public Long min() {
        return start;
    }

    public Long max() {
        return end;
    }

    @Override
    public String toString() {
        return get().toString();
    }
}
