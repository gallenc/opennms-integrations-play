# example messages

[examplemessages.json](..docs/examplemessages.json)  contains the rest message to send to the api


## ReSTER

ReSTer is an app for chrome or firefox which allos simple test ReST requests to be sent ver HTTP/HTTPS

[ReSTer chrome]( https://chrome.google.com/webstore/detail/rester/eejfoncpjfgmeleakejdcanedmefagga?hl=en)

[ReSTer firefox]( https://addons.mozilla.org/en-GB/firefox/addon/rester/)

[opennms-roadfault-rester.json](..docs/opennms-roadfault-rester.json) contains a ReSTER configuration to use with the example

Note you may need to change the root urls from http://localhost  to http://[::1]  if your setup uses ipv6

## OpenNMS:

getAlarms  gets an alarm list from the ReST api

getEvents  gets an event list from the ReST api

postEvent posts an alarmRaise event to the ReST api


## RoadfaultAPI:

PostRoadFaultEvent posts a road fault message to the simple ReST application for testingf

