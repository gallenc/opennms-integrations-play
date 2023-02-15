package org.opennms.examples.integration.exx1;

import org.opennms.features.kafka.producer.model.OpennmsModelProtos;
import org.opennms.features.kafka.producer.model.OpennmsModelProtos.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmMessageCallbackShortImpl implements AlarmMessageCallback {
	private static final Logger LOG = LoggerFactory.getLogger(AlarmMessageCallbackShortImpl.class);

	@Override
	public void onAlarmEvent(OpennmsModelProtos.Alarm alarmEvent) {
		
		String relatedAlarms = "related alarms count:"+alarmEvent.getRelatedAlarmCount()+" relatedAlarms: ";
		if(alarmEvent.getRelatedAlarmList() !=null) {
			for(Alarm a: alarmEvent.getRelatedAlarmList()) {
				relatedAlarms = relatedAlarms +" "+a.getId();
			}
		}
		
		
		LOG.info("***************************** callback received alarm message:"
				+ "\nId: " + 
				alarmEvent.getId()+
				"\nseverity: " + 
				alarmEvent.getSeverity()+
				"\nuei: "+
				alarmEvent.getUei()+
				"\nlog: "+
				alarmEvent.getLogMessage()+
				"\nreductionkey: "+
				alarmEvent.getReductionKey()+
				"\nnode: "+
				alarmEvent.getNodeCriteria()+
				relatedAlarms
				+"\n*****************************");

	}

}
