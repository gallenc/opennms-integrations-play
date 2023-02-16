# OpenNMS kafka alarms, drools and api experiments

This folder contains experimental example code to show how OpenNMS alarms can be processed using drools and forwarded using Kafaka.

It also shows how a new 'camera mib' can be imported into OpenNMS and used for testing alarm generation.

Please note this is work in progress.

## Kafka Integration Clients

[integration-example1](../integration-example1) contains a maven project  which creates a stand alone command line  kafka-client andl also a spring boot project (web-client) which shows collected alarms on a web ui.
both of these projects can be packaged in docker containers and run with a dockerised opennms using docker-compose.

To build the containers you need docker installed and use the profile 

```
mvn clean install -P docker

```

## Docker Compose Project
 
[minimal-minion-kafka](../minimal-minion-kafka) contains a docker compose project which runs OpenNMS and kafka and also can run the test containers

To start OpenNMS use

```
 docker-compose up -d
```
 
 To view the kafka messages use
 
```
 docker-compose  --profile kafka-client logs -f kafka-client
```