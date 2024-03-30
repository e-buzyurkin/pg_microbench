package tests.myTests.testUtils;

import tests.myTests.*;

public class RunTests {

    public static void main(String[] args) {
        Values.main(args);
        Function.main(args);
        Append.main(args);
        TestAggregate.main(args);
        WindowAgg.main(args);
        Gather.main(args);
        SemiJoin.main(args);
        AntiJoin.main(args);
        SubPlan.main(args);
    }
}
