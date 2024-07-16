package bench.v2.histogram;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramIterationValue;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Histogram2CSV {
    public static void write(Histogram histogram, String filename) {
        long bins = (long) Math.sqrt(histogram.getTotalCount()) + 1;

        long len = (long) ((histogram.getMaxValue() - histogram.getMinValue()) / (double) bins) + 1;

        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println("bucket,value,len");

            for (HistogramIterationValue x : histogram.linearBucketValues(len)) {
                pw.println(x.getValueIteratedTo() - len / 2.  + ", " + x.getCountAddedInThisIterationStep() + ", " + len);
            }
        } catch (FileNotFoundException ignored) {
        }
    }
}