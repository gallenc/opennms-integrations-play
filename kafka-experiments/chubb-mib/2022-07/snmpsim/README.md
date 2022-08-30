# Chubb mib definitions and simulation


## introduction

This simulator use the static snmpsim simulator to simulate a chubb camera mib

the version of the sipsim simulator is 0.4 and is packaged as a docker image

see https://github.com/tandrup/docker-snmpsim

see https://hub.docker.com/r/tandrup/snmpsim/tags

This online documentation points to a later version than the 0.4 image but is still useful (the names of the commands have changed)

https://snmplabs.thola.io/snmpsim/quickstart.html

https://www.ibm.com/support/pages/how-use-snmpsim-simulate-network-device-based-snmp-walk-file

However the actual documentation to use is in the 0.4 branch of the github repository.
 
https://github.com/etingof/snmpsim/blob/v0.4.7/docs/source/quickstart.rst

## current simulation

snmpsim detemines which data to use by the community name of the SNMP request. 
The community name determines which file in the /usr/local/snmpsim/data/ directory is read

3 files are injected by docker-compose

chubb.snmprec  is a native snmprec configuration with data manually taken from the supplied snmp walk

chubb1.snmpwalk is an snmp walk file using OID numbers only and not their text equivalent

chubb2.snmpwalk is an snmpwalk file using OID text. This cannot be parsed by snmpsim and causes it to crash. It may be that i am missing something here so it is included just to show the need to use raw OID's.


## to create a new simulation file directly from the mibs and not from snmpwalk

launch the docker image using docker compose and then open a shell 

```
docker-compose up -d

docker-compose exec snmpsim bash
```

once in the image first use the pysnmp library to compose the mib files to the pysnmp.py format (CHUBB-ROOT.py  CHUBB-TVBS-CAMERA-MIB.py,CHUBB-TVBS-MIB.py).
These are needed for the next step in generating the simulator

```
mibdump.py --debug=all --destination-directory=/usr/local/snmpsim/pysnmp_mibs/      /usr/local/snmpsim/mibs/CHUBB-TVBS-CAMERA.mib

ls /usr/local/snmpsim/pysnmp_mibs/
CHUBB-ROOT.py  CHUBB-TVBS-CAMERA-MIB.py
```
Now create the simulation data using snmpsim
```
mib2dev.py --output-file=/usr/local/snmpsim/data/chubb.snmprec --pysnmp-mib-dir=/usr/local/snmpsim/pysnmp_mibs   --mib-module=CHUBB-TVBS-CAMERA-MIB --table-size=1 --unsigned-range=1,8
```
this will generate a chubb.snmprec file in the snmpsim directory

The simulation can be accessed using community string 'chubb' which corresponds to the file name
   
