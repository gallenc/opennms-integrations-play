# Example ReST integration using OpenNMS scriptd

In this example a simple ReST api is implemented using spring boot in the [roadfaultapi project](../roadfaultapi).

This can be built and packaged as a docker container using the docker profile. 
To do this you must first have docker or docker-desktop installed on your host machine.


```
mvn clean install -P docker
```

Once created, you can run this container along side OpenNMS using the docker-compose script in [minimal-horizon](../minimal-horizon). 

The [minimal-horizon](../minimal-horizon) project also contains the example scriptd script which can send events to the ReST api.
 