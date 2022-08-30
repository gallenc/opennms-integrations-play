# Alarm List Viewer Kafka application

This is a very simple web application which uses the kafka client to view a list of alarms

## building
in parent project build using maven
```
mvn clean install
```

## running

you can run the built JAR directly in the IDE or on the command line ( in web/target directory)

```
java -jar kafka-web-0.0.1-SNAPSHOT.jar
```
or you can run using the maven spring boot plugin in the web module

```
mvn spring-boot:run
```

The ui is at http://localhost:8081/viewAlarms


# testing

You can send alarm raising and alarm clearing traps to the OpenNMS horizon instance

to get the internal address of the horizon instance use

```
 docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' horizon
```

Using ireasoning mibbrowser send traps to localhost:10162

On docker desktop, these traps will appear to have come from the host at 172.23.0.1

# testing using docker netsnmp (not correct yet)

Alternatively using netnmp in a  simple docker container (https://hub.docker.com/r/elcolio/net-snmp) you need to send the trap to the horizon container

#### panMotor raise
docker run --rm=true elcolio/net-snmp snmptrap -v 2c -c public 172.23.0.7:1162 ""  .1.3.6.1.4.1.52330.6.2.0.1        .1.3.6.1.4.1.52330.6.2.1.0  s xxxx   .1.3.6.1.4.1.52330.6.2.1.0 i 0  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### panMotor clear
docker run --rm=true elcolio/net-snmp snmptrap -v 2c -c public 172.23.0.7:10162 ""  .1.3.6.1.4.1.52330.6.2.0.1        .1.3.6.1.4.1.52330.6.2.1.0  s xxxx   .1.3.6.1.4.1.52330.6.2.1.0 i 0  .1.3.6.1.4.1.52330.6.2.5.0 i 0




# notes

OpenNMS protobuf file is here 

https://github.com/OpenNMS/opennms/blob/opennms-29.0.10-1/features/kafka/producer/src/main/proto/opennms-kafka-producer.proto

However this is not directly used in the OpenNMS build but hard coded into the model here

https://github.com/OpenNMS/opennms/blob/opennms-29.0.10-1/features/kafka/producer/src/main/java/org/opennms/features/kafka/producer/model/OpennmsModelProtos.java


see https://www.baeldung.com/spring-boot-jsp for web app explaination