package bench.v2.histogram;

import lombok.Getter;

@Getter
public enum TimeUnite {
    NANOSECOND("ns", 1),
    MICROSECOND("mcs", 1000),
    MILLISECOND("ms", 1000 * 1000),
    SECOND("s", 1000 * 1000 * 1000),;

    private final String name;
    private final long nanoInUnit;

    TimeUnite(String name, long nanoInUnit) {
        this.name = name;
        this.nanoInUnit = nanoInUnit;
    }
}
