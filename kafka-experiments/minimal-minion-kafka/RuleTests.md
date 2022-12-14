# Testing the chubb-rules

This page explains how to do simple tests on the chubb rules in OpenNMS.

## 1. pre-requisit -  install docker-compose and docker. 
On windows use [docker-desktop](https://www.docker.com/products/docker-desktop/)

## 2. start opennms in docker

in the minimal-minion-kafka project, issue the following command

```
docker-compose up -d
```
The -d option runs the containers in the background as a daemon

You can check if OpenNMS comes up correctly by watching the log

```
docker-compose logs -f horizon
```

The first iime you run the database is initialised and the startup will take a minute or so.
If there is fault in parsing the chubb-rules.drl file, the startup will fail and you can see the error in the log.

If all goes well,you will see in the final log entries
```
horizon  | [INFO] Invocation start successful for MBean OpenNMS:Name=JettyServer
...
horizon  | [INFO] Invocation start successful for MBean OpenNMS:Name=PerspectivePoller
```

use ctrl-c to exit the log

you should now be able to view the OpenNMS ui at
http://localhost:8980

if you have ipv6 on the host, you may need to use
http://[::1]:8980

username admin
password admin

look at the nodes page

## 3. you now need to add the cameranetsnmp1 node to opennms.

log into the running container to yield a bash prompt using

```
docker-compose exec cameranetsnmp1 bash
```
then ping the cameranetsnmp1 from within the container in order to find it's address in docker 
```
[root@cameranetsnmp1 /]# ping cameranetsnmp1
PING cameranetsnmp1 (172.25.0.5) 56(84) bytes of data.
64 bytes from cameranetsnmp1 (172.25.0.5): icmp_seq=1 ttl=64 time=0.027 ms
```
You can now provision a new node with node label cameranetsnmp1 in opennms with the given address (in this case 172.25.0.5).
(use the circle with + beside the cogs  the top bar to 'quick add node')

You should see the node listed on the nodes page (http://[::1]:8980/opennms/element/nodeList.htm)

Then send a cold start trap from ameranetsnmp1 to horizon which should create a cold start event on the new node.

```
snmptrap -v 2c -c public horizon SNMPv2-MIB::coldStart 0
```


## 4. use the following commands to create new alarms on opennms for cameranetsnmp1

After sending the first trap you should see a panMotor raise alarm and a group 5 situation alarm with on attached alarm.

After sending the second trap you should see a tiltMotor raise alarm and the group 5 situation should now have 2 alarms attached

```
#### panMotor raise
snmptrap -v 2c -c public horizon:1162 ""  .1.3.6.1.4.1.52330.6.2.0.1        .1.3.6.1.4.1.52330.6.2.1.0  s xxxx   .1.3.6.1.4.1.52330.6.2.1.0 i 0  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### tiltMotor raise
snmptrap -v 2c -c public horizon:1162 ""  .1.3.6.1.4.1.52330.6.2.0.1        .1.3.6.1.4.1.52330.6.2.1.0  s xxxx   .1.3.6.1.4.1.52330.6.2.1.0 i 1  .1.3.6.1.4.1.52330.6.2.5.0 i 1

```

## 5. use the following trap command to clear the alarms

After clearing both alarms, the situation should also clear.

```
#### panMotor clear
snmptrap -v 2c -c public horizon:1162 ""  .1.3.6.1.4.1.52330.6.2.0.1        .1.3.6.1.4.1.52330.6.2.1.0  s xxxx   .1.3.6.1.4.1.52330.6.2.1.0 i 0  .1.3.6.1.4.1.52330.6.2.5.0 i 0


#### tiltMotor clear
snmptrap -v 2c -c public horizon:1162 ""  .1.3.6.1.4.1.52330.6.2.0.1        .1.3.6.1.4.1.52330.6.2.1.0  s xxxx   .1.3.6.1.4.1.52330.6.2.1.0 i 1  .1.3.6.1.4.1.52330.6.2.5.0 i 0
```

##  6. Try other traps to see alarms raised in other groups

All of the possible traps are listed in [TrapExamplesAndNotesCHUBB-TVBS-CAMERA-MIB.md](../chubb-mib/2022-07/CHUBB/processedOpenNMS/TrapExamplesAndNotesCHUBB-TVBS-CAMERA-MIB.md)

You will need to substitute the node address localhost:10162 for the opennms address in docker (horizon:1162)



