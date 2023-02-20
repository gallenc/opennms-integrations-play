## OPENNMS OVERLAY

## update 20-2-2023

main changes in this version

chubb-rules.drl

* better handling of edge cases for OnmsSeverity in sendUdateSituationEvent
* added a message which shows in log including relatec alarmIds and severities. This is needed to force kafka notifier to see changes in situation caused by severity of alarm

alarmd.drl 

* changed clean up rule to run over window of 10 seconds instead of 5 minutes  this deletes cleared alarms almost immediately

situations.drl  - no changes from default

opennms.alarm.drools.situation.events.xml

* added update-on-reduction fields so that alarm fields are updated on every event. This forces the event to propogate over kafka

```
         <update-field field-name="descr" update-on-reduction="true" />
         <update-field field-name="logmsg" update-on-reduction="true" />
```

alarmd.properties

* make new alarm if previous has been cleared

```
org.opennms.alarmd.newIfClearedAlarmExists = true
```

experimental changes

These are various undocumented properties which speed up reaction to changes. They are left commented out so that defaults are used for now. It is not clear that they have the desired effect so I dont recommend changing these for now.

alarmlistener.properties

```
# snapshot update to alarms changed from 2 minutes to 30 sec
# https://github.com/OpenNMS/opennms/blob/develop/opennms-alarms/daemon/src/main/java/org/opennms/netmgt/alarmd/AlarmLifecycleListenerManager.java
# org.opennms.alarms.snapshot.sync.ms = 30000

```

drools.properties
```
# drools kick timer property changed from default 30s to 10s
# see https://github.com/OpenNMS/opennms/blob/develop/opennms-alarms/daemon/src/main/java/org/opennms/netmgt/alarmd/drools/ManagedDroolsContext.java

# org.opennms.netmgt.alarmd.drools.liveness_check_interval_ms = 10000
```

