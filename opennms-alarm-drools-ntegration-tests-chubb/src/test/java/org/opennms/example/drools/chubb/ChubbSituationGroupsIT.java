/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.example.drools.chubb;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.opennms.core.test.alarms.AlarmMatchers.acknowledged;
import static org.opennms.core.test.alarms.AlarmMatchers.hasSeverity;


import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.test.alarms.driver.ScenarioResults;
import org.opennms.core.test.alarms.driver.State;
import org.opennms.core.test.alarms.driver.extension.ScenarioExt;
import org.opennms.core.test.alarms.driver.Scenario;
import org.opennms.core.test.db.TemporaryDatabase;
import org.opennms.netmgt.events.api.EventConstants;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsEventParameter;
import org.opennms.netmgt.model.OnmsSeverity;

import com.google.common.collect.ImmutableMap;

/**
 * This test suite allows us to:
 *  A) Define and play out scenarios using timestamped events and actions.
 *  B) Playback the scenarios
 *  C) Analyze the state of alarms at various points in time.
 *  D) Analyze the state changes of a particular alarm over time.
 *
 * Using these tools we can validate the behavior of the alarms in various scenarios
 * without worrying about the underlying mechanics.
 *
 * @author jwhite
 */
public class ChubbSituationGroupsIT {
	
	// 3 nodes are predefined in smoke tests
	public static Integer NODE_1 = 1;
	public static Integer NODE_2 = 2;
	public static Integer NODE_3 = 3;
	
	@Before
	public void setproperties() {
		System.out.println("setting properties");
		System.setProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, "postgres");
		// ds.setPassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));

	}
	
	/**
	 * Testing groups - see chubb-rules
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/panMotor","group5");
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/panMotor-clear","group5");
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/tiltMotor","group5");
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/tiltMotor-clear","group5");
        
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/wiperMotor","group6");
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/wiperMotor-clear","group6");
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/heater","group6");
	 *  euiGroupMap.put("uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/heater-clear","group6");
	 */
	@Test
	public void chubbGroupSituationsTest() {
		 
        System.out.println("**** Chubb Situations Test Start ****");
        
	        // Alarms may not immediately clear/unclear due to the way to rules are structured
	        // so we add some delay between the steps to make sure that they do
	        long step = TimeUnit.MINUTES.toMillis(2);
	        Scenario scenario = ScenarioExt.builder()
	                .withLegacyAlarmBehavior()
	                .withTickLength(1, TimeUnit.MINUTES)
	                // long time, int nodeId, String uei,String clearUei, String severity, String source, Map<String,String> params
	                .withRaiseAlarmUeiEvent(step*1, NODE_1, "uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/panMotor", OnmsSeverity.WARNING.getLabel() , "test", null)
	                .withRaiseAlarmUeiEvent(step*2, NODE_1, "uei.opennms.org/alarms/drools/situation", OnmsSeverity.WARNING.getLabel() , "test", 
	                   		ImmutableMap.of("situationGroup","group5",
	                				"related-reductionKeyuei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/panMotor:1","uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/panMotor:1")
	                		)

	                .withRaiseAlarmUeiEvent(step*3, NODE_1, "uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/tiltMotor", OnmsSeverity.WARNING.getLabel() , "test", null)
	                .withRaiseAlarmUeiEvent(step*4, NODE_1, "uei.opennms.org/alarms/drools/situation", OnmsSeverity.WARNING.getLabel() , "test", 
	                		ImmutableMap.of("situationGroup","group5",
	                				"related-reductionKeyuei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/panMotor:1","uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/panMotor:1",
            				        "related-reductionKeyuei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/tiltMotor:1","uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/tiltMotor:1")
	                		)
	                
	                // long time, int nodeId, String uei,String clearUei, String severity, String source, Map<String,String> params
	                .withClearAlarmUeiEvent(step*5, NODE_1, "uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/panMotor-clear", "uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChange/tiltMotor", OnmsSeverity.CLEARED.getLabel() , "test", null)
	                .withClearAlarmUeiEvent(step*6, NODE_1, "uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/tiltMotor-clear", "uei.opennms.org/traps/CHUBB-TVBS-CAMERA-MIB/healthChangeClear/tiltMotor-clear", OnmsSeverity.CLEARED.getLabel() ,"test", null)
	                
	                // added to show alarm list clear from node 1
	                .withNodeDownEvent(step*7, NODE_2)
	                .withNodeUpEvent(step*8, NODE_2)
	                .build();
	        ScenarioResults results = scenario.play();
	        
	        // Verify the set of alarms at various points in time

	        long t=0*step; 
	        assertThat(results.getAlarms(t), hasSize(0));

	        List<OnmsAlarm> alarmList = results.getAlarms(0);
	        
	        t=0*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=1*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=2*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));

	        t=3*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=4*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=5*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=6*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=7*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        t=8*step; 
	        alarmList = results.getAlarms(t);
	        System.out.println(" alarms at time "+t+"  " +printAlarms(alarmList));
	        
	        alarmList = results.getAlarmsAtLastKnownTime();
	        System.out.println(" alarms getAlarmsAtLastKnownTime " +printAlarms(alarmList));
	        
	        
	        
	        //Integer alarmId = results.getAlarms(step*1).get(0).getId();
	        

	        
	        //assertThat(results.getProblemAlarm(step*1), hasSeverity(OnmsSeverity.MAJOR));
	        
	        // t=2, a (cleared) problem and a resolution
	        //assertThat(results.getAlarms(step*3), hasSize(2));
	        //assertThat(results.getAlarmAt(step*3, alarmId), hasSeverity(OnmsSeverity.CLEARED));

	}
	
	public String printAlarms(List<OnmsAlarm> alarmList){
		String msg="alarmList size: "+alarmList.size();
		for(OnmsAlarm alarm :alarmList) {
			msg=msg+"\n  "+(alarm.isSituation()? "situation: ":"alarm: ") +alarm;
			// cant do because alarm no longer in transaction
//			msg=msg+"  event params: ";	
//			for (OnmsEventParameter p :alarm.getEventParameters()) {
//				msg=msg+"{name="+p.getName()+" value="+p.getValue()+"} ";
//			}
			
		}
		return msg;
		
	}
	
    /**
     * Verifies the basic life-cycle of a trigger, followed by a clear.
     *
     * Indirectly verifies the cosmicClear and cleanUp automations.
     */
//    @Test
//    public void canTriggerAndClearAlarm() {
//        Scenario scenario = ScenarioExt.builder()
//                .withLegacyAlarmBehavior()
//                .withNodeDownEvent(1, 1)
//                .withNodeUpEvent(2, 1)
//                .build();
//        ScenarioResults results = scenario.play();
//
//        // Verify the set of alarms at various points in time
//
//        // t=0, no alarms
//        assertThat(results.getAlarms(0), hasSize(0));
//        // t=1, a single problem alarm
//        assertThat(results.getAlarms(1), hasSize(1));
//        assertThat(results.getProblemAlarm(1), hasSeverity(OnmsSeverity.MAJOR));
//        // t=2, a (cleared) problem and a resolution
//        assertThat(results.getAlarms(2), hasSize(2));
//        assertThat(results.getProblemAlarm(2), hasSeverity(OnmsSeverity.CLEARED));
//        assertThat(results.getResolutionAlarm(2), hasSeverity(OnmsSeverity.NORMAL));
//        // t=∞
//        assertThat(results.getAlarmsAtLastKnownTime(), hasSize(0));
//
//        // Now verify the state changes for the particular alarms
//
//        // the problem
//        List<State> problemStates = results.getStateChangesForAlarmWithId(results.getProblemAlarm(1).getId());
//        assertThat(problemStates, hasSize(3)); // warning, cleared, deleted
//        // state 0 at t=1
//        assertThat(problemStates.get(0).getTime(), equalTo(1L));
//        assertThat(problemStates.get(0).getAlarm(), hasSeverity(OnmsSeverity.MAJOR));
//        // state 1 at t=2
//        assertThat(problemStates.get(1).getTime(), equalTo(2L));
//        assertThat(problemStates.get(1).getAlarm(), hasSeverity(OnmsSeverity.CLEARED));
//        assertThat(problemStates.get(1).getAlarm().getCounter(), equalTo(1));
//        // state 2 at t in [5m2ms, 10m]
//        assertThat(problemStates.get(2).getTime(), greaterThanOrEqualTo(2L + TimeUnit.MINUTES.toMillis(5)));
//        assertThat(problemStates.get(2).getTime(), lessThan(TimeUnit.MINUTES.toMillis(10)));
//        assertThat(problemStates.get(2).getAlarm(), nullValue()); // DELETED
//
//        // the resolution
//        List<State> resolutionStates = results.getStateChangesForAlarmWithId(results.getResolutionAlarm(2).getId());
//        assertThat(resolutionStates, hasSize(2)); // cleared, deleted
//        // state 0 at t=2
//        assertThat(resolutionStates.get(0).getTime(), equalTo(2L));
//        assertThat(resolutionStates.get(0).getAlarm(), hasSeverity(OnmsSeverity.NORMAL));
//        // state 1 at t in [5m2ms, 10m]
//        assertThat(resolutionStates.get(1).getTime(), greaterThanOrEqualTo(2L + TimeUnit.MINUTES.toMillis(5)));
//        assertThat(resolutionStates.get(1).getTime(), lessThan(TimeUnit.MINUTES.toMillis(10)));
//        assertThat(resolutionStates.get(1).getAlarm(), nullValue()); // DELETED
//    }




}
