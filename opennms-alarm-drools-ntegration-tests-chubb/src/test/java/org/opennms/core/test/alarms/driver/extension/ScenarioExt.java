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

package org.opennms.core.test.alarms.driver.extension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.opennms.netmgt.alarmd.AlarmPersisterImpl;
import org.opennms.netmgt.events.api.EventConstants;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.xml.event.AlarmData;

import com.google.common.collect.ImmutableMap;

import org.opennms.core.test.alarms.driver.AcknowledgeAlarmAction;
import org.opennms.core.test.alarms.driver.Action;
import org.opennms.core.test.alarms.driver.JUnitScenarioDriver;
import org.opennms.core.test.alarms.driver.Scenario;
import org.opennms.core.test.alarms.driver.ScenarioResults;
import org.opennms.core.test.alarms.driver.SendEventAction;
import org.opennms.core.test.alarms.driver.UnAcknowledgeAlarmAction;

public class ScenarioExt extends Scenario {

    private final List<Action> actions;
    
    private final boolean legacyAlarmBehavior;

    private final Runnable awaitUntilRunnable;

    public final long tickLengthMillis;

    public ScenarioExt(ScenarioBuilderExt builder) {
    	super(builder);
        this.actions = new ArrayList<>(builder.actions);
        this.actions.sort(Comparator.comparing(Action::getTime));
        this.legacyAlarmBehavior = builder.legacyAlarmBehavior;
        this.awaitUntilRunnable = builder.awaitUntilRunnable;
        this.tickLengthMillis = builder.tickLengthMillis;
    }

    public List<Action> getActions() {
        return actions;
    }
    
    public boolean getLegacyAlarmBehavior() {
        return legacyAlarmBehavior;
    }

    public long getTickLengthMillis() {
        return tickLengthMillis;
    }

    public static ScenarioBuilderExt builder() {
        return new ScenarioBuilderExt();
    }

    public static class ScenarioBuilderExt  extends ScenarioBuilder{
        private final List<Action> actions = new ArrayList<>();
        
        private boolean legacyAlarmBehavior = false;

        private Runnable awaitUntilRunnable = () -> {};

        private long tickLengthMillis = 1;

        public ScenarioBuilderExt withTickLength(long duration, TimeUnit unit) {
            if (duration < 1) {
                throw new IllegalArgumentException("Duration must be strictly positive!");
            }
            this.tickLengthMillis = unit.toMillis(duration);
            return this;
        }

        public ScenarioBuilderExt withNodeDownEvent(long time, int nodeId) {
            EventBuilder builder = new EventBuilder(EventConstants.NODE_DOWN_EVENT_UEI, "test");
            builder.setTime(new Date(time));
            builder.setNodeid(nodeId);
            builder.setSeverity(OnmsSeverity.MAJOR.getLabel());

            AlarmData data = new AlarmData();
            data.setAlarmType(1);
            data.setReductionKey(String.format("%s:%d", EventConstants.NODE_DOWN_EVENT_UEI, nodeId));
            builder.setAlarmData(data);

            builder.setLogDest("logndisplay");
            builder.setLogMessage("testing");
            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }

        public ScenarioBuilderExt withNodeUpEvent(long time, int nodeId) {
            EventBuilder builder = new EventBuilder(EventConstants.NODE_UP_EVENT_UEI, "test");
            builder.setTime(new Date(time));
            builder.setNodeid(nodeId);
            builder.setSeverity(OnmsSeverity.NORMAL.getLabel());

            AlarmData data = new AlarmData();
            data.setAlarmType(2);
            data.setReductionKey(String.format("%s:%d", EventConstants.NODE_UP_EVENT_UEI, nodeId));
            data.setClearKey(String.format("%s:%d", EventConstants.NODE_DOWN_EVENT_UEI, nodeId));
            builder.setAlarmData(data);

            builder.setLogDest("logndisplay");
            builder.setLogMessage("testing");
            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }

        // Create an event with lower severity
        public ScenarioBuilderExt withInterfaceDownEvent(long time, int nodeId) {
            EventBuilder builder = new EventBuilder(EventConstants.INTERFACE_DOWN_EVENT_UEI, "test");
            builder.setTime(new Date(time));
            builder.setNodeid(nodeId);
            builder.setSeverity(OnmsSeverity.MINOR.getLabel());

            AlarmData data = new AlarmData();
            data.setAlarmType(1);
            data.setReductionKey(String.format("%s:%d", EventConstants.INTERFACE_DOWN_EVENT_UEI, nodeId));
            builder.setAlarmData(data);

            builder.setLogDest("logndisplay");
            builder.setLogMessage("testing");
            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }
        
        /**
         * additional method to inject an event
         * @param time
         * @param nodeId
         * @param uei
         * @param clearUei
         * @param severity
         * @param source
         * @param params
         * @return
         */
        public ScenarioBuilderExt withClearAlarmUeiEvent(long time, int nodeId, String uei, String clearUei ,String severity, String source, Map<String,String> params) {
            EventBuilder builder = new EventBuilder(uei, source);
            builder.setTime(new Date(time));
            builder.setNodeid(nodeId);
            builder.setSeverity(severity);

            AlarmData data = new AlarmData();
            data.setReductionKey(String.format("%s:%d", uei, nodeId));
            if(clearUei==null) {
                data.setAlarmType(1);
            } else {
                data.setAlarmType(2);
                data.setClearKey(String.format("%s:%d", clearUei, nodeId));
            }

            builder.setAlarmData(data);
            
            if (params!=null) for (Entry<String,String> e: params.entrySet()) {
               builder.addParam(e.getKey(), e.getValue());
           }

            builder.setLogDest("logndisplay");
            builder.setLogMessage("testing");
            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }

        
        /**
         * additional method to inject an type 2 event
         * @param time
         * @param nodeId
         * @param uei
         * @param clearUei
         * @param severity
         * @param source
         * @param params
         * @return
         */
        public ScenarioBuilderExt withRaiseAlarmUeiEvent(long time, int nodeId, String uei,String severity, String source, Map<String,String> params) {
            EventBuilder builder = new EventBuilder(uei, source);
            builder.setTime(new Date(time));
            builder.setNodeid(nodeId);
            builder.setSeverity(severity);

            AlarmData data = new AlarmData();
            data.setReductionKey(String.format("%s:%d", uei, nodeId));
 
                data.setAlarmType(1);

            builder.setAlarmData(data);
            
            if (params!=null) for (Entry<String,String> e: params.entrySet()) {
               builder.addParam(e.getKey(), e.getValue());
           }

            builder.setLogDest("logndisplay");
            builder.setLogMessage("testing");
            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }
        
        /**
         * additional method to inject an type 3 event
         * @param time
         * @param nodeId
         * @param uei
         * @param clearUei
         * @param severity
         * @param source
         * @param params
         * @return
         */
        public ScenarioBuilderExt withUnclearableAlarmUeiEvent(long time, int nodeId, String uei,String severity, String source, Map<String,String> params) {
            EventBuilder builder = new EventBuilder(uei, source);
            builder.setTime(new Date(time));
            builder.setNodeid(nodeId);
            builder.setSeverity(severity);

            AlarmData data = new AlarmData();
            data.setReductionKey(String.format("%s:%d", uei, nodeId));
 
                data.setAlarmType(3);

            builder.setAlarmData(data);
            
            if (params!=null) for (Entry<String,String> e: params.entrySet()) {
               builder.addParam(e.getKey(), e.getValue());
           }

            builder.setLogDest("logndisplay");
            builder.setLogMessage("testing");
            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }


        public ScenarioBuilderExt withAcknowledgmentForNodeDownAlarm(long time, int nodeId) {
            actions.add(new AcknowledgeAlarmAction("test", new Date(time), String.format("%s:%d", EventConstants.NODE_DOWN_EVENT_UEI, nodeId)));
            return this;
        }

        public ScenarioBuilderExt withUnAcknowledgmentForNodeDownAlarm(long time, int nodeId) {
            actions.add(new UnAcknowledgeAlarmAction("test", new Date(time), String.format("%s:%d", EventConstants.NODE_DOWN_EVENT_UEI, nodeId)));
            return this;
        }

        public ScenarioBuilderExt withAcknowledgmentForSituation(long time, String situtationId) {
            actions.add(new AcknowledgeAlarmAction("test", new Date(time), String.format("%s:%s", EventConstants.SITUATION_EVENT_UEI, situtationId)));
            return this;
        }

        public ScenarioBuilderExt withUnAcknowledgmentForSituation(long time, String situtationId) {
            actions.add(new UnAcknowledgeAlarmAction("test", new Date(time), String.format("%s:%s", EventConstants.SITUATION_EVENT_UEI, situtationId)));
            return this;
        }

        public ScenarioBuilderExt withSituationForNodeDownAlarms(long time, String situtationId, int... nodesIds) {
            EventBuilder builder = new EventBuilder(EventConstants.SITUATION_EVENT_UEI, "test");
            builder.setTime(new Date(time));
            builder.setSeverity(OnmsSeverity.NORMAL.getLabel());
            for (int k = 0; k < nodesIds.length; k++) {
                final String reductionKey = String.format("%s:%d", EventConstants.NODE_DOWN_EVENT_UEI, nodesIds[k]);
                builder.addParam(AlarmPersisterImpl.RELATED_REDUCTION_KEY_PREFIX + k, reductionKey);
            }

            AlarmData data = new AlarmData();
            data.setAlarmType(3);
            data.setReductionKey(String.format("%s:%s", EventConstants.SITUATION_EVENT_UEI, situtationId));
            builder.setAlarmData(data);

            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }

        // create a situation using reduction keys
        public ScenarioBuilderExt withSituationForAlarmReductionKeys(long time, String situtationId, String... alarms) {
            EventBuilder builder = new EventBuilder(EventConstants.SITUATION_EVENT_UEI, "test");
            builder.setTime(new Date(time));
            builder.setSeverity(OnmsSeverity.NORMAL.getLabel());
            for (int k = 0; k < alarms.length; k++) {
                builder.addParam(AlarmPersisterImpl.RELATED_REDUCTION_KEY_PREFIX + k, alarms[k]);
            }

            AlarmData data = new AlarmData();
            data.setAlarmType(3);
            data.setReductionKey(String.format("%s:%s", EventConstants.SITUATION_EVENT_UEI, situtationId));
            builder.setAlarmData(data);

            actions.add(new SendEventAction(builder.getEvent()));
            return this;
        }
        
        public ScenarioBuilderExt withLegacyAlarmBehavior() {
            legacyAlarmBehavior = true;
            return this;
        }

        public ScenarioBuilderExt awaitUntil(Runnable runnable) {
            this.awaitUntilRunnable = Objects.requireNonNull(runnable);
            return this;
        }

        public Scenario build() {
            return new ScenarioExt(this);
        }

    }

    public ScenarioResults play() {
        final JUnitScenarioDriver driver = new JUnitScenarioDriver();
        return driver.run(this);
    }

    public void awaitUntilComplete() {
        awaitUntilRunnable.run();
    }
}
