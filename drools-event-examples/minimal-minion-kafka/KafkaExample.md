# Example Drools Event manipulation


## Setting up opennms-kafka-producer test environment

Provides a running OpenNMS with a kafka broker and a minion.



Forked from https://github.com/opennms-forge/stack-play

OpenNMS horizon docker images are here
https://hub.docker.com/u/opennms

## setup
Install docker / docker compose of your development machine.
On a PC you can use docker-desktop

the .env file sets the version of OpenNMS to use - currently 29.0.11
(see https://docs.docker.com/compose/environment-variables/)


##To run OpenMMS

```
docker-compose up -d
```

eventually you should see the OpenNMS UI at http://localhost:8980
(note if you are usign windows with ipv6 you may need to use http://[::1]:8980)

you should also see the kafka ui at http://localhost:8080/

PS - to end the tests use

```
docker-compose down
```
and to also clear the database volumes

```
docker-compose down -v
```

you can reach inside hte opennms container using

```
docker-compose exec horizon bash
```

# To activate and test alarms forwarder.

Based on https://docs.opennms.com/horizon/29/operation/kafka-producer/kafka-producer.html
(see also https://rmoff.net/2018/08/02/kafka-listeners-explained/)

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

check feature is installed
admin@opennms()> feature:list | grep opennms-kafka-producer
opennms-kafka-producer                      | 29.0.6            | x        | Started     | opennms-29.0.6                    | OpenNMS :: Kafka :: Producer
```
However we can provide this configuration permanently to OpenNMS by adding the following files to the configuration in the meridian opennms etc directory. (Note - for some reason this should but  doesn't work - you need to configure the image manually)

create the file org.opennms.features.kafka.producer.client.cfg and add contents

```
bootstrap.servers = broker:29092
```

This file is put in the docker-compose project
```
container-fs\horizon\opt\opennms-overlay\etc\org.opennms.features.kafka.producer.client.cfg 
```

Also added opennms-kafka-producer feature into org.apache.karaf.features.cfg before the line opennms-karaf-health

```
...
  opennms-search, \
  opennms-kafka-producer, \
  opennms-karaf-health

```

to test alarm list within the karaf cli, use the following karaf cli commands. 
(note if no alarms in system, these lines will be empty).

```
admin@opennms()> opennms:kafka-sync-alarms
Performing synchronization of alarms from the database with those in the ktable.
Executed 0 updates in 3ms.

Number of reduction keys in ktable: 1
Number of reduction keys in the db: 1 (1 alarms total)
admin@opennms()> opennms:kafka-list-alarms
uei.opennms.org/internal/importer/importSuccessful:file:/opt/opennms/etc/imports/Minions.xml
        OpenNMS-defined internal event: importer process successfully completed
```



