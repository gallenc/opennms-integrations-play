# Example ReST integration using OpenNMS scriptd

## introduction

This example shows how to use OpenNMS to forward event information to a simple ReST API using scriptd.
A simple simulator is provided which can receive ReST calls and display them on a web ui.
This is quite a complex example but hopefully it will provide inspiration for scripting api's to other systems.

beanshell https://beanshell.github.io/ is used in this example. 
Other scripting languages such as groovey could be used as an alternative. 
Beanshell is basically scripted java and can use any imported java class from the OpenNMS class path. 
It does however have certain unexpected 'features' which you need to watch for. 
The java written in beanshell resembles java 1.4 which most importantly means that it does not support generics. Read the beanshell documentation. 

When I wrote this, I wrote and tested the more complex code (i.e. the async client) in java first and then translated it to beanshell. 

When testing in OpenNMS, you need to look at the scriptd.log for errors and debug messages.

## video 
A youtube video for this exampe is available here https://youtu.be/snl0QNedIYI 

This video provides an example of a simple integration from OpenNMS to a ReST api based on certain opennms events. The video complements the code example and documentation on github 
https://github.com/gallenc/opennms-integrations-play/tree/main/onmsIntegrationExample1/roadfaultintegration

and also references the opennms example scriptd configuration here

opennms /etc/examples 
https://github.com/OpenNMS/opennms/blob/develop/opennms-base-assembly/src/main/filtered/etc/examples/scriptd-event-forwarder.bsh

https://github.com/OpenNMS/opennms/blob/develop/opennms-base-assembly/src/main/filtered/etc/examples/scriptd-configuration.xml

opennms scriptd executor code

https://github.com/OpenNMS/opennms/blob/develop/opennms-services/src/main/java/org/opennms/netmgt/scriptd/Executor.java

## ReST api details

This section describes the ReST api in the form of a specification received from the end user

The Service Health Endpoint web-based API will use the HTTP protocol to transport the data
and all payloads will be in JSON format. The interface consists of a single HTTP POST method
and uses HTTP Basic Authentication. We would expect service health suppliers to provide
service health updates whenever the health status of an inventory item changes.

### Request Message Body

```
[
   {
      "source":"APM-Checkmk",
      "equipmentClass":"u_server_cluster",
      "equipmentReference":"openshift12345",
      "statusTime":"2020-03-26T10:15:40.857Z",
      "statusInformation":[
        {
           "statusName":"AV Program Status",
           "statusValue":"WARN",
           "statusAdditionalInfo":""
        }
      ]
    }
]
```

As shown above, messages are sent to the service health endpoint in JSON format. 
The body should contain an array of service health objects. 
A more detailed explanation of a service health object is described below:

* source– A unique identifier will be provided by the customer to each supplier for each source of data for which they are providing service health information

* equipmentClass – To be agreed between equipment supplier and data supplier. Will be used in conjunction with the equipmentReference to identify which inventory item is being referred to

* equipmentReference – To be agreed between equipment supplier and data supplier. Will be
used in conjunction with the equipmentClass to identify which inventory item is being
referred to

* statusTime – The time that the service health information was recorded. Must be in ISO-
8601 format with millisecond precision

* statusInformation – An array of status information objects. 

A status information object contains:

* statusName – The name of the status attribute of the inventory item for which service
health information is being provided
* statusValue – The status value to be used for the status name. Can be:

| HEALTHY |The given status name has no issues|
| FAULTY | The given status name has a fault with no further granularity available|
| WARN | The given status name has a fault that should be treated as a warning|
| CRITICAL | The given status name has a fault that should be treated as critical|

* statusAdditionalInfo – Optional free text description which can be used to provide more
information to the  operators

### Success Responses

Http Code 201

Content-Type application/json

Body Returns the current payload 

### Application error response

Http Code 400

Content-Type application/json

Body

```
{
  "error_message": "The type of error that has been reported”
  "error_type": "Message detailing the error"
}
```

### Application unauthorised response

Http Code 401

Content-Type application/json

Body

```
 {
"timestamp": 1586879649998,
"status": 401,
"error": "Unauthorized",
"message": "Bad credentials",
"
```

## running the example

In this example a simple ReST api is implemented using spring boot in the [roadfaultapi project](../roadfaultintegration/roadfaultapi).

This can be built and packaged as a docker container using the docker profile. 
To do this you must first have docker or docker-desktop installed on your host machine.

```
mvn clean install -P docker
```

Once created, you can run this container along side OpenNMS using the docker-compose script in [minimal-horizon](../roadfaultintegration/minimal-horizon). 

to start

```
 docker-compose --profile roadfaultapi up -d
```

To view the simple ReST service browse to http://localhost:8080/home

A Rester plugin configuration is provided in the [rester](../roadfaultintegration/rester) folder to allow you to test sending events to the api or to OpenNMS 

The [minimal-horizon](../roadfaultintegration/minimal-horizon) project also contains the example scriptd script which can send events to the ReST api.
This is described below.

# Scriptd ReST API

The OpenNMS code is contained in the scriptd configuration which is in the  [container-fs/opt/opennms-overlay/etc/](../roadfaultintegration/minimal-horizon/container-fs/opt/opennms-overlay/etc/) folder of the docker-compose project

 [scriptd-configration.xml](../roadfaultintegration/minimal-horizon/container-fs/opt/opennms-overlay/etc/scriptd-configration.xml)

## Async Client

Scriptd is single threaded and each script call must return very quickly to avoid blocking the thread. 
A synchronous HTTP ReST client could be used to send messages but there is no guarantee that the API will respond quickly enough to meet the scriptd contract.

For this reason an asynchronous client is used which will dispatch the request in a separate thread and return to scriptd with minimal delay. 

[scriptedApacheHttpAsyncClient-v1.8.bsh](../roadfaultintegration/minimal-horizon/container-fs/opt/opennms-overlay/etc/scriptedApacheHttpAsyncClient-v1.8.bsh) contains the asynchronous client code written in beanshell for sending json messages to a ReST interface. 

The client uses the Apache HttpAsyncClient which is included in the OpenNMS library. 

The client has methods for http GET, POST, PUT DELETE and PATCH calls. 
In this example we simple use the POST call in the event script.

```
public void postRequest(String url, String jsonMessage, String username, String password)
```

The client must first be instantiated and started in the start-script so that it is available for events. 

Similarly it must be shut down in the stop-script 

The client also has a blocking queue which is used to buffer responses from each call to async client.
A separate listener thread pulls these responses and can be used to initiate further actions through a callback api depending on the response received. In the default case the listener simply logs the response.

## API implementation

The [scriptd-configration.xml](../roadfaultintegration/minimal-horizon/container-fs/opt/opennms-overlay/etc/scriptd-configration.xml) script contains the business logic for the api client in OpenNMS.

A beanshell senderUtils() class is defined in the start-script which contains methods to map OpenNMS event severities into the Api's 4 severities and to construct the json message to send.

The event-script checks events received and sends a message for only the selected events. 
Event log and description fields are used to construct the message along with the node label. 
Jsoup is used to remove all HTML tags and line feeds from the event text before it is added to the json message.

 
