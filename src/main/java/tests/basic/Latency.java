package tests.basic;

import bench.v2.histogram.Histogram2CSV;
import bench.v2.query.SQL;
import org.HdrHistogram.Histogram;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static bench.V2.args;
import static bench.V2.db;

public class Latency {
    public static void main(String[] args) {
        Histogram histogram = new Histogram(30000000000L, 5);

        args(args);

        try (Connection connection = db.getDataSource().getConnection()) {
            SQL s = new SQL(connection);

            for (int i = 0; i < 10000; i++) {
                s.sql("select 1", histogram);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        System.out.println(histogram.getTotalCount());
        Histogram2CSV.write(histogram, "hist.csv");
    }
}
