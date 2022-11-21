# test docker compose

This docker compose project is based off Oct 10 2022

https://github.com/opennms-forge/stack-play/tree/master/full-stack

It may be you will need to update this to your needs

docker-compose logs -f horizon

ssh -p 8101 admin@localhost

admin@opennms()> kar:list
KAR Name
-----------------------------------
trace-route-plugin-kar-1.0-SNAPSHOT

admin@opennms()> feature:list | grep plugin
sample-plugin-common                        | 0.8.0.SNAPSHOT   |          | Uninstalled | opennms-integration-api-features            | Sample Plugin :: Common
opennms-plugin-sample                       | 0.8.0.SNAPSHOT   |          | Uninstalled | opennms-integration-api-features            | Sample Plugin :: OpenNMS
minion-plugin-sample                        | 0.8.0.SNAPSHOT   |          | Uninstalled | opennms-integration-api-features            | Sample Plugin :: Minion
opennms-plugins-trace-route-plugin          | 1.0.0.SNAPSHOT   |          | Uninstalled | openmms-plugins-trace-route-plugin-features | OpenNMS :: Plugins :: trace-route-plugin
okhttp                                      | 3.10.0           |          | Uninstalled | openmms-plugins-trace-route-plugin-features | okhttp
guava                                       | 30.1.1.jre       |          | Started     | openmms-plugins-trace-route-plugin-features | guava
jackson                                     | 2.11.1           |          | Uninstalled | openmms-plugins-trace-route-plugin-features | jackson



problem:
admin@opennms()> feature:install opennms-plugins-trace-route-plugin
Error executing command: Unable to resolve root: missing requirement [root] osgi.identity; osgi.identity=opennms-plugins-trace-route-plugin; type=karaf.feature; version="[1.0.0.SNAPSHOT,1.0.0.SNAPSHOT]"; filter:="(&(osgi.identity=opennms-plugins-trace-route-plugin)(type=karaf.feature)(version>=1.0.0.SNAPSHOT)(version<=1.0.0.SNAPSHOT))" [caused by: Unable to resolve opennms-plugins-trace-route-plugin/1.0.0.SNAPSHOT: missing requirement [opennms-plugins-trace-route-plugin/1.0.0.SNAPSHOT] osgi.identity; osgi.identity=org.opennms.plugin.experimental.trace-route-plugin-plugin; type=osgi.bundle; version="[1.0.0.SNAPSHOT,1.0.0.SNAPSHOT]"; resolution:=mandatory [caused by: Unable to resolve org.opennms.plugin.experimental.trace-route-plugin-plugin/1.0.0.SNAPSHOT: missing requirement [org.opennms.plugin.experimental.trace-route-plugin-plugin/1.0.0.SNAPSHOT] osgi.wiring.package; filter:="(&(osgi.wiring.package=org.opennms.integration.api.v1.alarms)(version>=1.2.0)(!(version>=2.0.0)))"]]
admin@opennms()> feature:list | grep integration.api
opennms-integration-api                     | 0.8.0.SNAPSHOT   |          | Started     | opennms-integration-api-features            | OpenNMS Integration API
sample-plugin-common                        | 0.8.0.SNAPSHOT   |          | Uninstalled | opennms-integration-api-features            | Sample Plugin :: Common
opennms-plugin-sample                       | 0.8.0.SNAPSHOT   |          | Uninstalled | opennms-integration-api-features            | Sample Plugin :: OpenNMS
minion-plugin-sample                        | 0.8.0.SNAPSHOT   |          | Uninstalled | opennms-integration-api-features            | Sample Plugin :: Minion
admin@opennms()> Received disconnect from ::1 port 8101:2: Detected IdleTimeout after 1800306/1800000 ms.
Disconnected from ::1 port 8101