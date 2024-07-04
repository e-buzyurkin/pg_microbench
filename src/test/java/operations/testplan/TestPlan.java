package operations.testplan;

import operations.bpftrace.BpfTraceRunner;
import operations.utils.TestCLI;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static bench.V2.closeConnection;

public class TestPlan {
    private BpfTraceRunner runner;

    @BeforeTest
    public void setUp() {
        runner = new BpfTraceRunner();
        runner.startBpfTrace();
        TestCLI.connectToDatabase();
    }

    @AfterTest
    public void tearDown() {
        runner.stopBpfTrace();
        closeConnection();
    }
}
