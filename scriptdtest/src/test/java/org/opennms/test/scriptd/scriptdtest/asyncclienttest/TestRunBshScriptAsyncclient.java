package org.opennms.test.scriptd.scriptdtest.asyncclienttest;

import java.util.Date;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opennms.netmgt.xml.event.Event;

/**
 * This test simulates the initiation on a beahshell script in an OpenNMS scriptd context.
 * It allows you to test the start, onevent and stop scripts as they will be created in the scriptd-configuration.xml
 * Because the test is  situated in a class path which includes the key OpenNMS classes, you
 * should be able to recreate the real onEvent situation.
 * 
 * The bean lookup comes from the tutorial 
 * https://javasourcequery.com/example/org.apache.bsf.lookupBean
 * @author cgallen
 */
//
public class TestRunBshScriptAsyncclient {
    static final Logger log = LoggerFactory.getLogger(TestRunBshScriptAsyncclient.class);

    private static BSFManager mgr = new BSFManager();

    private BSFEngine beanshellEngine;

    @Before
    public void before() throws BSFException {
        log.debug("executing start script");
        // register beanshell with the BSF framework
        String[] extensions = { "bsh" };
        BSFManager.registerScriptingEngine("beanshell", "bsh.util.BeanShellBSFEngine", extensions);
        beanshellEngine = mgr.loadScriptingEngine("beanshell");
        mgr.registerBean("log", log);

        String startScript = "     log = bsf.lookupBean(\"log\"); \n"
                + "   log.debug(\"start script logging enabled before importing source\"); \n"
                + "   source(\"src/main/resources/asyncclienttest/scriptd-start-script.bsh\"); \n";

        mgr.exec("beanshell", "", 0, 0, startScript);

        log.debug("finished executing start script");

    }

    @After
    public void after() throws BSFException {
        log.debug("executing stop script");
        
        log.debug("waiting for messages to complete");
        try {
            // pause for 20 secs
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            log.debug("sleep interrupted");
        }

        String stopScript = "     log = bsf.lookupBean(\"log\"); \n" 
                + "   log.debug(\"running stop script\"); \n"
                + "   source(\"src/main/resources/asyncclienttest/scriptd-stop-script.bsh\"); \n";

        mgr.exec("beanshell", "", 0, 0, stopScript);
        
        log.debug("executed stop script");

    }

    
    @Test
    public void test() throws BSFException {
        log.debug("start of onevent test script");

        Event event = new Event();
        event.setUei("uei.opennms.org/internal/alarms/AlarmRaised");
        event.setHost("127.0.0.1");
        event.setCreationTime(new Date());

        mgr.registerBean("event", event);

        String onEventScript = "     log = bsf.lookupBean(\"log\"); \n" 
                + "   log.debug(\"running onEvent script\"); \n"
                + "   source(\"src/main/resources/asyncclienttest/scriptd-event-script.bsh\"); \n";

        mgr.exec("beanshell", "", 0, 0, onEventScript);

        log.debug("end of onevent test script");
    }
}
