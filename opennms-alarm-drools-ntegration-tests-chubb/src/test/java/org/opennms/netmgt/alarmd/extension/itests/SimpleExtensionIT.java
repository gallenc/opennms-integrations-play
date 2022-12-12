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

package org.opennms.netmgt.alarmd.extension.itests;

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
import org.opennms.netmgt.model.OnmsSeverity;

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
public class SimpleExtensionIT {
	
	@Before
	public void setproperties() {
		System.out.println("setting properties");
		System.setProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, "postgres");
		// ds.setPassword(System.getProperty(TemporaryDatabase.ADMIN_PASSWORD_PROPERTY, TemporaryDatabase.DEFAULT_ADMIN_PASSWORD));

	}

    /**
     * Verifies the basic life-cycle of a trigger, followed by a clear.
     */

    @Test
    public void canTriggerAndClearAlarm() {
        long step = TimeUnit.MINUTES.toMillis(2);
        Scenario scenario = ScenarioExt.builder()
                .withLegacyAlarmBehavior()
                .withTickLength(1, TimeUnit.MINUTES)
                // long time, int nodeId, String uei,String clearUei, String severity, String source, Map<String,String> params
                .withRaiseAlarmUeiEvent(step*1, 1, EventConstants.NODE_DOWN_EVENT_UEI, OnmsSeverity.MAJOR.getLabel() , "test", null)
                // long time, int nodeId, String uei,String clearUei, String severity, String source, Map<String,String> params
                .withClearAlarmUeiEvent(step*2, 1, EventConstants.NODE_UP_EVENT_UEI, EventConstants.NODE_DOWN_EVENT_UEI, OnmsSeverity.NORMAL.getLabel() , "test", null)
                .withClearAlarmUeiEvent(step*3, 1, EventConstants.NODE_UP_EVENT_UEI, EventConstants.NODE_DOWN_EVENT_UEI,OnmsSeverity.NORMAL.getLabel() , "test", null)
                .build();
        ScenarioResults results = scenario.play();


        // Verify the set of alarms at various points in time

        // t=0, no alarms
        assertThat(results.getAlarms(0), hasSize(0));
        // t=1, a single problem alarm
        assertThat(results.getAlarms(step*1), hasSize(1));
        Integer alarmId = results.getAlarms(step*1).get(0).getId();
        
        assertThat(results.getProblemAlarm(step*1), hasSeverity(OnmsSeverity.MAJOR));
        
        // t=2, a (cleared) problem and a resolution
        assertThat(results.getAlarms(step*3), hasSize(2));
        assertThat(results.getAlarmAt(step*3, alarmId), hasSeverity(OnmsSeverity.CLEARED));

    }


}
