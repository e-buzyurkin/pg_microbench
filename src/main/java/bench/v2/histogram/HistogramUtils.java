package bench.v2.histogram;

import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramIterationValue;

import java.io.*;
import java.nio.file.Files;

public class HistogramUtils {
    private static final String PYTHON_SCRIPT = "histogram.py";

    public static TimeUnite hist2csv(Histogram histogram, String filename) {
        long bins = (long) Math.sqrt(histogram.getTotalCount() * 0.995) + 1;

        long maxValue = histogram.getValueAtPercentile(99.5);
        long minValue = histogram.getMinValue();

        TimeUnite timeUnite = TimeUnite.NANOSECOND;

        for (TimeUnite tu : TimeUnite.values()) {
            if (tu.getNanoInUnit() > timeUnite.getNanoInUnit() && minValue > tu.getNanoInUnit()) {
                timeUnite = tu;
            }
        }

        long len = (long) ((maxValue - minValue) / (double) bins) + 1;

        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println("bucket,value,len");

            for (HistogramIterationValue x : histogram.linearBucketValues(len)) {
                if (x.getValueIteratedTo() > maxValue) {
                    break;
                }
                pw.println((x.getValueIteratedTo() - len / 2.) / timeUnite.getNanoInUnit()  + ", "
                        + x.getCountAddedInThisIterationStep() + ", " + ((double) len) / timeUnite.getNanoInUnit());
            }
        } catch (FileNotFoundException ignored) {
        }

        return timeUnite;
    }

    public static void csv2png(String inputCsv, String outputPng, String unitOfMeasurement) {
        if (!(new File("./" + PYTHON_SCRIPT).exists())) {
            throw new RuntimeException("There is no python script: " + PYTHON_SCRIPT);
        }


        String[] command = {
                "python",
                PYTHON_SCRIPT,
                unitOfMeasurement,
                inputCsv,
                outputPng
        };

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void hist2png(Histogram histogram, String csv, String png) {
        TimeUnite timeUnite = hist2csv(histogram, csv);

        csv2png(csv, png, timeUnite.getName());
    }
}