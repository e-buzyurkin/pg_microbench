package operations;
import operations.utils.TestUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.testng.annotations.BeforeClass;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestAggregate.class,
        TestAppend.class,
        TestCTEScan.class,
        TestDelete.class,
        TestFinalizeAggregate.class,
        TestForeignScan.class,
        TestFunctionScan.class,
        TestGather.class,
        TestGatherMerge.class,
        TestGroup.class,
        TestGroupAggregate.class,
        TestHashAggregate.class,
        TestHashAntiJoin.class,
        TestHashJoin.class,
        TestHashSemiJoin.class,
        TestHashSetOp.class,
        TestIncrementalSort.class,
        TestInsert.class,
        TestLockRows.class,
        TestMaterialize.class,
        TestMemoize.class,
        TestMerge.class,
        TestMergeAppend.class,
        TestMixedAggregate.class,
        TestNestedLoop.class,
        TestParallelAppend.class,
        TestParallelHash.class,
        TestParallelHashJoin.class,
        TestParallelSeqScan.class,
        TestProjectSet.class,
        TestRecursiveUnion.class,
        TestResult.class,
        TestSampleScan.class,
        TestSeqScan.class,
        TestSetOp.class,
        TestSort.class,
        TestSubPlan.class,
        TestSubqueryScan.class,
        TestTableFunctionScan.class,
        TestTidScan.class,
        TestUnique.class,
        TestUpdate.class,
        TestValues.class,
        TestWindowAgg.class,
        TestWorkTableScan.class
})

public class TestSuite {

}
