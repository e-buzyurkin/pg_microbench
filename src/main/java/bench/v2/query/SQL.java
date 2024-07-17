package bench.v2.query;

import lombok.RequiredArgsConstructor;
import org.HdrHistogram.Histogram;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
public class SQL implements AutoCloseable {
    private final Connection connection;
    private final int fetchSize = 1000;

    public void sql(String sql, Object... binds) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < binds.length; i++) {
                pstmt.setObject(i + 1, binds[i]);
            }
            pstmt.setFetchSize(fetchSize);

            pstmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void sql(String sql, Histogram latencyHist) {
        try (Statement statement = connection.createStatement()) {

            long start = System.nanoTime();
            statement.execute(sql);
            latencyHist.recordValue(System.nanoTime() - start);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
