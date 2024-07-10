
This is benchmark for every query type.

## Technologies used
1) Java
2) Maven
3) TestNG
4) bpfTrace

## Config
Install "bpftrace" and "bpftrace-dbgsym"

```
echo "deb http://ddebs.ubuntu.com $(lsb_release -cs) main restricted universe multiverse
deb http://ddebs.ubuntu.com $(lsb_release -cs)-updates main restricted universe multiverse
deb http://ddebs.ubuntu.com $(lsb_release -cs)-proposed main restricted universe multiverse" | \
sudo tee -a /etc/apt/sources.list.d/ddebs.list
sudo apt install ubuntu-dbgsym-keyring
sudo apt update
sudo apt install bpftrace-dbgsym
```
## How to run 

### Linux

All tests:

```
mvn test -DargLine="-ea -Dargs='-h localhost -p 5432'" -f pom.xml
```
Single test:
```
mvn test -Dtest=TestResult -DargLine="-ea -Dargs='-h localhost -p 5432'" -f pom.xml
```
Tests with profiling required root rights (bpftrace)
```
sudo mvn test -DargLine="-ea -Dargs='-h localhost -p 5432 -profiling'" -f pom.xml
```
```
sudo mvn test -Dtest=TestResult -DargLine="-ea -Dargs='-h localhost -p 5432 -profiling'" -f pom.xml
```

Drop all created during tests tables:
```
mvn compile exec:java -Dexec.mainClass="utils.DropTables" -Dexec.args="-h localhost -p 5432" -f pom.xml
```
Without compile:
```
mvn exec:java -Dexec.mainClass="utils.DropTables" -Dexec.args="-h localhost -p 5432" -f pom.xml
```

## Args

```
 -c <concurrency>   amount of concurrent workers. Defaults to 10
 -d <database>      database name. Defaults to 'postgres'
 -h <host>          database host name
 -l <cnTimeLimit>   max life time of connection in seconds. Disabled by
                    default
 -o <run type>      Run type (generate,run). Defaults to EXECUTE
 -p <port>          database port. Defaults to 5432
 -P <password>      user password
 -profiling         enable profiling in benchmark
 -s <timeout>       Warker distribute strategy. Default to none
 -t <timeout>       test duration. Default to 10
 -T <txLimit>       max amount of transactions. Disabled by default
 -U <username>      user name. Defaults to 'postgres'
 -v <volume>        Volume size. Defaults to 10
 -w <workers>       amount of workers. Defaults to 5
```

All test classes:
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

### Results
#### logger
```
bench.log
```
#### you can copy and paste this .txt file in excel tables using ";" to split into columns.
#### WARNING: don't forget manually clean up .txt file after test session.
```
results.txt
```