# notes

## kafka client

This project contains a simple kafka client example with some tests which will work if you run the OpenNMS kafka docker compose

OpenNMS protobuf file is here 
https://github.com/OpenNMS/opennms/blob/opennms-29.0.10-1/features/kafka/producer/src/main/proto/opennms-kafka-producer.proto

However this is not directly used in the OpenNMS build but hard coded into the model here
https://github.com/OpenNMS/opennms/blob/opennms-29.0.10-1/features/kafka/producer/src/main/java/org/opennms/features/kafka/producer/model/OpennmsModelProtos.java

## simple web app

The simple web application uses the client to display a simple web page corresponding to the state of the alarm list in OpenNMS.
See instructions for use in the web folder .
