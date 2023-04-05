# Example OpenNMS use of Scriptd for integration

This docker-compose project can run a small web service which accepts OpenNMS posts

First build the roadfaultintegration project using the docker profile

```
mvn clean install -P docker

```

You can then start and stop opennms and the roadfaultapi container in this project.
(You should have docker installed on your host).

```
docker-compose  --profile roadfaultapi  up -d

## to see the logs use
docker-compose  --profile roadfaultapi  logs -f roadfaultapi

## to shut down use
docker-compose  --profile roadfaultapi down

to see opennms

http://[::1]:8980/opennms/   username admin password admin
or
http://localhost:8980/opennms/

to see roadfaultapi

http://[::1]:8080/
or
http://localhost:8080/


```

## to view OpenNMS logs as opennms starts up use

```
 docker-compose --profile roadfaultapi logs -f horizon
```

## to open a terminal in the running opennms container use

```
docker-compose --profile roadfaultapi exec horizon bash
```

