# Scheduler
Microservice for scheduling Kafka messages to trigger the call for Hansard data updates.

## Main stack
- Spring Boot
- Java 11
- Apache Kafka
- Apache Avro
- Confluent schema-registry
- Flyway
- Postgres

## Running the service
Run docker-compose:
```shell script
git clone https://github.com/bgasparotto-engineering/scheduler
cd scheduler
docker-compose up -d
```

Then run the main class `SchedulerApplication.java`

## Interacting with the service
1. Hit the endpoint http://localhost:10003/management/trigger-update-now
2. Check the logs where the produced messages will be displayed as a result;
3. Visit the Schema Registry at http://localhost:8081/subjects and you should see the created subject by Avro.
4. Visit the Control Center at http://http://localhost:9091 to find details about the Kafka cluster.

### Generating Avro source code
This project uses [Gradle Avro Plugin](https://github.com/davidmc24/gradle-avro-plugin) for generating Java classes for 
schemas defined in `.avsc` files:
```shell script
./gradlew generateAvroJava
```
