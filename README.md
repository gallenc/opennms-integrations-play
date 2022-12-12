# opennms-integrations-play
Miscellaneous work to show integrations to / from OpenNMS.

## Kafka Experiments and experimental drools rules
This is an overlay project containing example CHUBB camera alarm creation and forwarding through kafka bus and also drools examples
[kafka-experiments(../opennms-integrations-play/kafka-experiments/kafka-experiments)

### chubb mib 
Creation of chubb alarm objects from chub mib files
[chubb-mib](../opennms-integrations-play/kafka-experiments/chubb-mib) 

### integration-example-1
Simple kafka alarm client and a web ui which receives alarms from OpenNMS over kafka alarm forwarding bus
[integration-example-1](../opennms-integrations-play/kafka-experiments/integration-example-1) 

### minimal-minion-kafka
Example OpeNNMS configuration using chubb event definitions ande chubb-rules.drl file to create alarms
[minimal-minion-kafka](../opennms-integrations-play/kafka-experiments/minimal-minion-kafka) 

## pris-docker-compose
[pris-docker-compose](../opennms-integrations-play/pris-docker-compose) 
is an example project for using pris with an excel spreadsheet to provision OpenNMS


## iptablesexample1
[iptablesexample1](../opennms-integrations-play/iptablesexample1)
Is a simple example of using iptables to map ports to differnt ip addresses so that OpenNMS can poll snmp agents on differnt ports.

## multi-opennms-newts-docker
[multi-opennms-newts-docker](../opennms-integrations-play/multi-opennms-newts-docker)
is a simple project with several opennms pointing at single casandra
Incomplete



## onmsIntegrationExample1
example integrating openNMS to simple ReST api for TTOC solution - not used in project
[onmsIntegrationExample1](../opennms-integrations-play/onmsIntegrationExample1)

# scriptdtest
example project integrating scriptd
[scriptdtest](../opennms-integrations-play/scriptdtest)


## events to rester
[eventstorester](../opennms-integrations-play/eventstorester) 
is an example project to create a rester postman configuration from OpenNMS eventconfig.xml
Incomplete




