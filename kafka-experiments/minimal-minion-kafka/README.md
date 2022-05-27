# Kafka / opennms docker compose

provides a running OpenNMS with a kafka broker and a minion.

Forked from https://github.com/opennms-forge/stack-play

OpenNMS horizon docker images are here
https://hub.docker.com/u/opennms

## setup
Install docker / docker compose of your development machine.
On a PC you can use docker-desktop

the .env file sets the version of opennms to use - currently 29.0.3
(see https://docs.docker.com/compose/environment-variables/)


##To run OpenMMS

```
docker-compose up -d
```

# To activate and test alarms forwarder.
Based on https://docs.opennms.com/horizon/29/operation/kafka-producer/kafka-producer.html

Please note that the kafka broker in docker-compose is running on port 29092  not 9092 as described in the docs.

You need to get access to the OpenNMS Karaf shell. 
There is an SSH client installed on the minion image but not the horizon image - so we ssh into the horizon opennms from the minion

note that docker-compose sets up the service names broker, horizon, database as dns names so ssh etc can reference these

To manually set up the alarm forwarder following the documentation
```
docker-compose exec minion bash
ssh -p 8101 admin@horizon

admin@opennms()> config:edit org.opennms.features.kafka.producer.client
admin@opennms()> config:property-set bootstrap.servers broker:29092
admin@opennms()> config:update
admin@opennms()> feature:install opennms-kafka-producer

check feature installed
admin@opennms()> feature:list | grep opennms-kafka-producer
opennms-kafka-producer                      | 29.0.3           | x        | Started     | opennms-29.0.3                   | OpenNMS :: Kafka :: Producer
```
However we can provide this configration permanently to OpenNMS by adding the following files to the configuration in the meridian opennms etc directory

create the file org.opennms.features.kafka.producer.client.cfg and add contents
bootstrap.servers = broker:29092

This file is put in the docker-compose project
container-fs\horizon\opt\opennms-overlay\etc\org.opennms.features.kafka.producer.client.cfg before the line opennms-karaf-health

Also added feature into org.apache.karaf.features.cfg
```
...
  opennms-search, \
  opennms-kafka-producer, \
  opennms-karaf-health

```

to test use
```
admin@opennms()> opennms:kafka-list-alarms
admin@opennms()> opennms:kafka-sync-alarms
```



