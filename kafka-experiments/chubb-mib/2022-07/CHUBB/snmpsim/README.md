# Chubb mib definitions and simulation

needs changed to new mib

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
The docker compose script injects the chubb.snmprec file ready for serving. This has been enhanced from the simple file (chubbfrommib.snmprec) generated using the instructions below. 
I have added sysOid objects so that OpenNMS can detect the device type.

In future when we can actually generate a simulation using an SNMP walk of the camera. The walk data will be enough to drive a static simulation

## to create a new simulation file from the mibs

launch the docker image using docker compose and then open a shell 
```
docker-compose up -d
docker-compose exec snmpsim bash
```

once in the image first use the pysnmp library to compole the mib files to the pysnmp .py format (CHUBB-ROOT.py  CHUBB-TVBS-CAMERA-MIB.py).
These are needed for the next step in genrerating he simulator

```
mibdump.py --debug=all --destination-directory=/usr/local/snmpsim/pysnmp_mibs/      /usr/local/snmpsim/mibs/CHUBB-TVBS-CAMERA.mib

ls ls /usr/local/snmpsim/pysnmp_mibs/
CHUBB-ROOT.py  CHUBB-TVBS-CAMERA-MIB.py
```
Now create the simulation data using snmpsim
```
mib2dev.py --output-file=/usr/local/snmpsim/data/chubb.snmprec --pysnmp-mib-dir=/usr/local/snmpsim/pysnmp_mibs   --mib-module=CHUBB-TVBS-CAMERA-MIB --table-size=1 --unsigned-range=1,8
```
this will generate a chubb.snmprec file in the snmpsim directory

The simulation can be accessed using community string 'chubb' which corresponds to hte file name
   
