
## Health Change notification

### Values
healthChange notification  oid .1.3.6.1.4.1.52330.6.2.0.1

varbind 1: healthChangeReason oid .1.3.6.1.4.1.52330.6.2.1.0

values  INTEGER {panMotor(0),tiltMotor(1),zoomMotor(2),apertureMotor(3),focusMotor(4),wiperMotor(5),heater(6),fluidLevel(7),
videoSignal(8), housingTamper(9), washerMotorFault(10), configPlugFault(11), tvbuCameraCommsFault(12) }

varbind 2: faultState oid .1.3.6.1.4.1.52330.6.2.5.0

values:  INTEGER {clear(0), triggered(1) }

### Test traps

#### panMotor raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 0  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### panMotor clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 0  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### tiltMotor raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 1  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### tiltMotor clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 1  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### zoomMotor raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 2  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### zoomMotor clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 2  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### apertureMotor raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 3  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### apertureMotor clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 3  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### focusMotor raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 4  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### focusMotor clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 4  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### wiperMotor raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 5  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### wiperMotor clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 5  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### heater raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 6  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### heater clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 6  .1.3.6.1.4.1.52330.6.2.5.0 i 0


  
#### fluidLevel raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 7  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### fluidlevel clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 7 .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### videoSignal raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 8  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### videoSignal clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 8  .1.3.6.1.4.1.52330.6.2.5.0 i 0

#### housingTamper raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 9  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### housingTamper clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 9  .1.3.6.1.4.1.52330.6.2.5.0 i 0


#### washerMotorFault raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 10  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### washerMotorFault clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 10  .1.3.6.1.4.1.52330.6.2.5.0 i 0

 

#### configPlugFault raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 11  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### configPlugFault clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 11  .1.3.6.1.4.1.52330.6.2.5.0 i 0



#### tvbuCameraFault raise
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 12  .1.3.6.1.4.1.52330.6.2.5.0 i 1

#### tvbuCameraFault clear
snmptrap -v 2c -c public localhost:10162 "" .1.3.6.1.4.1.52330.6.2.0.1 .1.3.6.1.4.1.52330.6.2.1.0 i 12  .1.3.6.1.4.1.52330.6.2.5.0 i 0


