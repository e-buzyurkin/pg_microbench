package tests.basic;

import bench.v2.histogram.HistogramUtils;
import bench.v2.query.SQL;
import org.HdrHistogram.Histogram;

import static bench.V2.args;
import static bench.V2.db;

public class Latency {
    public static void main(String[] args) {
        Histogram histogram = new Histogram(30000000000L, 5);

        args(args);

        try (SQL s = new SQL(db.getDataSource().getConnection())) {

            int n = 5000;

            for (int i = 1; i <= 100; i++) {
                for (int j = 0; j < n / 100; j++) {
                    s.sql("select 1", histogram);
                }
                System.out.println("Done: " + i + "%");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        System.out.println(histogram.getTotalCount());

        HistogramUtils.hist2png(histogram, "hist.csv", "hist.png");
    }
}
