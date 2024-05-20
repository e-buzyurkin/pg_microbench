package operations.testplan;

import operations.utils.TestCLI;
import org.junit.After;
import org.junit.Before;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import static bench.V2.closeConnection;

public class TestPlan {

    @Before
    public void setUp() {
        TestCLI.connectToDatabase();
    }

    @After
    public void tearDown() {
        closeConnection();
    }
}
