## How to build

```
mvn package
```

## How to run

```
env TEST_CONCURRENCY=25 bm ansible play -book test.yml
```

## How to deploy

export JAVA_VERSION=11
mvn deploy -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true -Dmaven.resolver.transport=wagon
