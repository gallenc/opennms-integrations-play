# Example OpenNMS using wsman to manage local windows host

This docker-compose project contains a waman configuration

You can then start and stop opennms 

(You should have docker installed on your host).

```
docker-compose   up -d

## to see the logs use
docker-compose   logs -f horizon

## to shut down use
docker-compose  down

to see opennms

http://[::1]:8980/opennms/   username admin password admin
or
http://localhost:8980/opennms/

```

## to view OpenNMS logs as opennms starts up use

```
 docker-compose --profile roadfaultapi logs -f horizon
```

## to open a terminal in the running opennms container use

```
docker-compose --profile roadfaultapi exec horizon bash
```

