# snmp agent address/port translation example 1

## use case
In this scenario a single box exposes multiple agents in the same ip address but on different ports. 
This example shows how centos8 nftables can be used to locally map each of these ports onto a different IP address only visable to the local machine. 
This would allow OpenNMS to use standard node definitions for each device allbeit with the ip address only having a symbolic meaning.
This scenario does not address the problem of correctly handling traps from the agents


## Overview
I have used a Centos8 docker image with systemd/init as the base. 
It should be possible to add this configuration into a Centos8 server running OpenNMS or running an OpenNMS minion

The example uses nftables which is the newer replacement for iptables and is standard with Centos8.
The example seems to work OK in a docker container on centos8 but will not work on docker-desktop on windows as the underlying firewall is differnt
 

## Simple test harness 

This example uses docker-compose to create 3 snmp agents running on different ports.

In this case, we are using docker-compose simply to create the snmpd agents and dicker directly to create a client with nftables enabled.

## start snmp containers

```
docker-compose -f ./docker-compose-netsnmp.yaml up -d

to shut down

docker-compose -f ./docker-compose-netsnmp.yaml down

```
## create a test container to test nftables

using docker to create and log into a small centos systemd container
```
docker run -d --name systemd-centos8 --cap-add=NET_ADMIN --cap-add=NET_RAW --tmpfs /tmp --tmpfs /run --tmpfs /run/lock -v /sys/fs/cgroup:/sys/fs/cgroup:ro jrei/systemd-centos:8
```

log into container

```
docker container exec -it systemd-centos8 bash

```
once inside container, install netsnmp and nftables
```
dnf install -y net-tools
dnf install -y nano
dnf install -y net-snmp-utils
dnf install -y nftables

systemctl start nftables
systemctl enable nftables
```
The systemd-centos8 container should be able to see the host gateway network on 172.17.0.1
```
ping 72.17.0.1
```
Check that the container can see the three snmpd images running on the host with different port addresses
```
(cameranetsnmp1)
snmpwalk -v1 -c public  172.17.0.1:11161
(cameranetsnmp2)
snmpwalk -v1 -c public  172.17.0.1:12161
(cameranetsnmp3)
snmpwalk -v1 -c public  172.17.0.1:12161
```
These should give the corresponding image name in the SNMPv2-MIB::sysObjectID.0 snmp result

```
snmpwalk -v1 -c public  172.17.0.1:11161
SNMPv2-MIB::sysDescr.0 = STRING: Linux cameranetsnmp1 4.18.0-240.15.1.el8_3.x86_64 #1 SMP Mon Mar 1 17:16:16 UTC 2021 x86_64
SNMPv2-MIB::sysObjectID.0 = OID: NET-SNMP-MIB::netSnmpAgentOIDs.10
DISMAN-EVENT-MIB::sysUpTimeInstance = Timeticks: (244137) 0:40:41.37
SNMPv2-MIB::sysContact.0 = STRING: Root <root@localhost> (configure /etc/snmp/snmp.local.conf)
SNMPv2-MIB::sysName.0 = STRING: cameranetsnmp1
...
```
Now we want to use nftables to allow these three agents on seperate ports to appear to the systemd-centos8 container on differnt ip addresses

inside the container create/edit a file containing the nftables configuration.
```
nano examplenftconfig.txt
```
add the following contents
```
table ip nat {
       chain OUTPUT {
                type nat hook output priority -100; policy accept;
                ip daddr 10.1.2.11 udp dport { 161 }  dnat to 192.168.172.1:11161;
                ip daddr 10.1.2.12 udp dport { 161 }  dnat to 192.168.172.1:12161;
                ip daddr 10.1.2.13 udp dport { 161 }  dnat to 192.168.172.1:13161;

                ip daddr 10.1.2.11 icmp type echo-request dnat to 192.168.172.1;
                ip daddr 10.1.2.12 icmp type echo-request dnat to 192.168.172.1;
                ip daddr 10.1.2.13 icmp type echo-request dnat to 192.168.172.1;
        }
}

```
Now flush the existing nftable rules and import the example configuration above.
Check that the configuration has loaded by listing the ruleset

```
nft flush ruleset            # this clears the existing configrution
nft -f examplenftconfig.txt  # this loads a configuration from a file
nft list ruleset             # this lists the current running rule set

```
You should now be able to ping the new agent addresses and do an snmp walk of each agent

```
ping 10.1.2.11
snmpwalk -v1 -c public  10.1.2.11:161

```
The above example does the following mapping

| SNMP Agent               | docker-compose service | corresponding ip address |
| ------------------------ | ---------------------- | ------------------------ |
| 192.168.172.1:11161      | cameranetsnmp1         | 10.1.2.11:161            |
| 192.168.172.1:12161      | cameranetsnmp2         | 10.1.2.12:161            |
| 192.168.172.1:13161      | cameranetsnmp2         | 10.1.2.13:161            |

Note that this allows an snmp client on the host to see each of the snmp agents on a different IP address.

However it does not map any spontaneous traps from the agents. 
THese would need to be mapped using an 'event translator'.


