## How to run

# All tests:

```
mvn test -DargLine="-ea -Dargs='-h 10.7.1.25 -p 5433'" -f pom.xml
```
# Single test:
```
mvn test -DTest=TestResult -DargLine="-ea -Dargs='-h 10.7.1.25 -p 5433'" -f pom.xml
```

## Args

```
 -c <concurrency>   amount of concurrent workers. Defaults to 10
 -d <database>      database name. Defaults to 'postgres'
 -h <host>          database host name
 -l <cnTimeLimit>   max life time of connection in seconds. Disabled by
                    default
 -o <run type>      run type (generate,run). Defaults to EXECUTE
 -p <port>          database port. Defaults to 5432
 -P <password>      user password
 -s <timeout>       warker distribute strategy. Default to none
 -t <timeout>       test duration. Default to 10
 -T <txLimit>       max amount of transactions. Disabled by default
 -U <username>      user name. Defaults to 'postgres'
 -v <volume>        volume size. Defaults to 10
 -w <workers>       amount of workers. Defaults to 5
```

## All test classes:
```
TestAggregate
TestAppend
TestCTEScan
TestDelete
TestFinalizeAggregate
TestForeignScan
TestFunctionScan
TestGather
TestGatherMerge
TestGroup
TestGroupAggregate
TestHashAggregate
TestHashAntiJoin
TestHashJoin
TestHashSemiJoin
TestHashSetOp
TestIncrementalSort
TestInsert
TestLockRows
TestMaterialize
TestMemoize
TestMerge
TestMergeAppend
TestMixedAggregate
TestNestedLoop
TestParallelAppend
TestParallelHash
TestParallelHashJoin
TestParallelSeqScan
TestProjectSet
TestRecursiveUnion
TestResult
TestSampleScan
TestSeqScan
TestSetOp
TestSort
TestSubPlan
TestSubqueryScan
TestTableFunctionScan
TestTidScan
TestUnique
TestUpdate
TestValues
TestWindowAgg
TestWorkTableScan
```
