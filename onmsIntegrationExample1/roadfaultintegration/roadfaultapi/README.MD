# Service Status message simulator
This application provides a simple ReST API for testing OpenNMS alarm forwarding.

This has been built using Spring (mostly Spring Boot but with some departures).

The main page provides a realtime view of incoming messages and replies.
 
If a username is set, then the system expects basic authentication messages.

If an error_message is set, then the system will always reply with this message.

A static test message can be sent to the system to check it is listening for messages using 'send test message'.


The log page privides a historical log of messages received and replies sent. 
The log is held in an in memory database and will be lost if he applicatino is restarted.

## build and run

If you have maven installed use
```
mvn clean install
mvn spring-boot:run
```

Alternatively you can use a provided maven installation 
```
 .\mvnw.cmd clean install
 .\mvnw.cmd spring-boot:run
```

The application will be available at http://localhost:8080/home

The ReST end point is http://localhost:8080/serviceHealth

## Message format details


```
URL https://localhost:8080/serviceHealth
Authorization Type Basic Auth (username and password provided separately)
Content-Type application/json
Body [
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

source– A unique identifier will be provided by T-TOC to each supplier for each source of
data for which they are providing service health information

equipmentClass – To be agreed between T-TOC supplier and data supplier. Will be used in
conjunction with the equipmentReference to identify which inventory item is being referred to
equipmentReference – To be agreed . Will be
used in conjunction with the equipmentClass to identify which inventory item is being
referred to

statusTime – The time that the service health information was recorded. Must be in ISO-
8601 format with millisecond precision

statusInformation – An array of status information objects. A status information object
contains:
– statusName – The name of the status attribute of the inventory item for which service
health information is being provided

– statusValue – The status value to be used for the status name. Can be:

HEALTHY – The given status name has no issues

FAULTY – The given status name has a fault with no further granularity available

WARN – The given status name has a fault that should be treated as a warning

CRITICAL – The given status name has a fault that should be treated as critical

statusAdditionalInfo – Optional free text description which can be used to provide more
information to the TOC operators

### Success
Http Code 201
Content-Type application/json
Body Returns the current payload as shown in 2.2.1

### Errors 
Http Code 400
Content-Type application/json
Body {
"error_message": "The type of error that has been reported”
"error_type": "Message detailing the error"
}

### Error unauthorised
Http Code 401
Content-Type application/json
Body {
"timestamp": 1586879649998,
"status": 401,
"error": "Unauthorized",
"message": "Bad credentials",
"path": "/serviceHealth"
}