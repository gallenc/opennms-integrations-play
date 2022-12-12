# Drools Alarm Tester

This project shows how to extend the internal OpenNMS Alarmd integration tests to allow external testing of customer specific drools rules.
The project is external to the opennms build and allows rules to be tested outside of the OpenNMS build cycle.

## OpenNMS code extensions

The external integration tests are based on an extended version of the alarm rules integration tests in 
[AlarmdBlackboxIT.java](https://github.com/OpenNMS/opennms/blob/develop/opennms-alarms/integration-tests/src/test/java/org/opennms/netmgt/alarmd/itests/AlarmdBlackboxIT.java )
which allows test scenarios to be constructed using a fluent builder like so:
```
Scenario scenario = Scenario.builder()
                .withLegacyAlarmBehavior()
                .withNodeDownEvent(1, 1)
                .withNodeUpEvent(2, 1)
                .build();
        ScenarioResults results = scenario.play();
```

These tests rely on the SenarioBuilder class declared within [Scenario.java](https://github.com/OpenNMS/opennms/blob/develop/core/test-api/alarms/src/main/java/org/opennms/core/test/alarms/driver/Scenario.java)

In our tests we have extended the SenarioBuilder  builder in 
[ScenarioExt.java](../opennms-alarm-drools-ntegration-tests-chubb/src/test/java/org/opennms/core/test/alarms/driver/extension/ScenarioExt.java)

This extension adds 3 additional methods which can directly create events with specific ueis. 
(These additional methods may be added to future opennms ScenarioBuilder through a pull request)
```
   public static class ScenarioBuilderExt  extends ScenarioBuilder{
   ...

  // injects a type 1 event
   public ScenarioBuilderExt withRaiseAlarmUeiEvent(long time, int nodeId, String uei,String severity, String source, Map<String,String> params) {
  
   // injects a type 2 event
   public ScenarioBuilderExt withClearAlarmUeiEvent(long time, int nodeId, String uei, String clearUei ,String severity, String source, Map<String,String> params) {

   // injects a type 3 event
   public ScenarioBuilderExt withUnclearableAlarmUeiEvent(long time, int nodeId, String uei,String severity, String source, Map<String,String> params) {

```

The 
[AlarmdBlackboxExtensionIT.java](../opennms-alarm-drools-ntegration-tests-chubb/src/test/java/org/opennms/netmgt/alarmd/extension/itests/AlarmdBlackboxExtensionIT.java)
class re-implements the  
[AlarmdBlackboxIT.java](https://github.com/OpenNMS/opennms/blob/develop/opennms-alarms/integration-tests/src/test/java/org/opennms/netmgt/alarmd/itests/AlarmdBlackboxIT.java)
class using 
[ScenarioExt.java](../opennms-alarm-drools-ntegration-tests-chubb/src/test/java/org/opennms/core/test/alarms/driver/extension/ScenarioExt.java) 
instead of 
[Scenario.java)](https://github.com/OpenNMS/opennms/blob/develop/core/test-api/alarms/src/main/java/org/opennms/core/test/alarms/driver/Scenario.java)

A simple test using the new methods in 
[ScenarioExt.java](../opennms-alarm-drools-ntegration-tests-chubb/src/test/java/org/opennms/core/test/alarms/driver/extension/ScenarioExt.java) 
is provided in 
[SimpleExtensionIT.java](../opennms-alarm-drools-ntegration-tests-chubb/src/test/java/org/opennms/netmgt/alarmd/extension/itests/SimpleExtensionIT.java) 


## Example CHUBB Rules Test
[ChubbSituationGroupsIT.java](../opennms-alarm-drools-ntegration-tests-chubb/src/test/java/org/opennms/example/drools/chubb/ChubbSituationGroupsIT.java)
provides a set of tests for the rules in 
[chubb-rules.drl](../opennms-alarm-drools-ntegration-tests-chubb/src/test/resources/opennms-base-assembly-overload/src/main/filtered/etc/alarmd/drools-rules.d/chubb-rules.drl)
The actual rules are described elsewhere

The rules need matching event definitions which are provided in 
[eventconfig.xml](../opennms-alarm-drools-ntegration-tests-chubb/src/test/resources/opennms-base-assembly-overload/src/main/filtered/etc/eventconfig.xml
and
[events](../opennms-alarm-drools-ntegration-tests-chubb/src/test/resources/opennms-base-assembly-overload/src/main/filtered/etc/events



## Building and running
### build OpenNMS
Currently OpenNNMS release repositories are not being populated properly. 
So to build and run this test suit, you first need to have checked out and built the version of OpenNMS you are targeting.

See [how to compile opennms](https://opennms.discourse.group/t/how-to-compile-opennms/486)
and 
[Build from source](https://docs.opennms.com/horizon/30/development/build-from-source.html)
This will build the jars needed for this test and place the in your local .m2 repository

The underlying integration test suit on which this test suit extends has a hard coded expectation of the location of certain required files.
In this project these files are checked out and located where they can be found by the test classes.

The build first downloads the liquibase files to create the database and places these in the 
```
core/schema/src/main/liquibase
``` 
directory at the base of the maven project.

Then the build downloads the etc files and places them in the 
```
opennms-base-assembly
```
directory at the base of the maven project.

Finally local configurations and drools files used in this test are copied from the project 
[opennms-base-assembly-overload](../opennms-alarm-drools-ntegration-tests-chubb/src/test/resources/opennms-base-assembly-overload)

```
resources/opennms-base-assembly-overload
```
 to overlay the opennms-base-assembly folder.

### modify the pom version 

The test suit
[parent pom.xml](../opennms-alarm-drools-ntegration-tests-chubb/pom.xml)
should have its parent pom section
```
	<parent>
		<groupId>org.opennms</groupId>
		<artifactId>opennms-alarms</artifactId>
		<version>30.0.0-1</version>
	</parent>
```
 set to match the vesion in the parent pom of the required OpenNMS build. 
In this case matching 
 [OpenNMS 30.0.0 parent pom.xml](https://github.com/OpenNMS/opennms/blob/opennms-30.0.0-1/pom.xml)
(An OpenNMS maven bom might be possible - but I haven investigated)

### run a database image and start the tests
The tests themselves need a postgresql instance running in order to complete.
A docker compose script is provided for this in

[docker-compose](../opennms-alarm-drools-ntegration-tests-chubb/docker-compose)

The database is spun up using
```
docker-compose up
```
The docker-compose configuration contains a script which creates an administrator role user for postgresql (username postgres password postgres) and an opennms database (username opennms password popennms) 

once you have compiled opennms you can compile and run the tests using

```
mvn clean install
```

### Dependencies

You can install a drools editor in eclipse from the eclipse update site

* Horizon 2023 is based off Meridian 27
* version of drools - 7.37.0.Final
* eclipse drools ide update site https://download.jboss.org/drools/release/7.37.0.Final/org.drools.updatesite/
