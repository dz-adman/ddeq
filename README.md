# dynamodb-autoconfiguration

Dynamo-DB Spring-Boot AutoConfiguration for
- **DynamoDbClient** (AWS SDK for Java 2.x)

To add the dependency in your project:
#### Maven
```
<dependency>
    <groupId>com.dz.dynamodb</groupId>
    <artifactId>ddconfig-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
#### Gradle
```
implementation "com.dz.dynamodb:ddconfig-spring-boot-starter:0.0.1-SNAPSHOT"
```

### application.properties
```
dynamodb.endpoint=
dynamodb.region=
dynamodb.accessKey=
dynamodb.secretKey=
dynamodb.apiCallTimeout=
dynamodb.numRetries=
```
### application.yml
```
dynamodb:
    endpoint:
    region:
    accessKey:
    secretKey:
    apiCallTimeout:
    numRetries:
```
<br>

> To disable the beans creation, set
> ```dynamodb.enabled``` property to ```false```
>
