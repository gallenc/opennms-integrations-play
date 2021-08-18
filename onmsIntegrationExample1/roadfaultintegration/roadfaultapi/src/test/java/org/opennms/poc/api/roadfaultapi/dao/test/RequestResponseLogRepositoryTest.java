/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.dao.test;

import java.util.Date;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.opennms.poc.api.roadfaultapi.dao.RequestResponseLogRepository;
import org.opennms.poc.api.roadfaultapi.model.AuthError;
import org.opennms.poc.api.roadfaultapi.model.ErrorMessage;
import org.opennms.poc.api.roadfaultapi.model.MessageContent;
import org.opennms.poc.api.roadfaultapi.model.RequestResponseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author cgallen
 */
@RunWith(SpringJUnit4ClassRunner.class)
// ApplicationContext will be loaded from the OrderServiceConfig class
@ContextConfiguration(classes = DAOTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class RequestResponseLogRepositoryTest {

    private static final Logger LOG = LogManager.getLogger(RequestResponseLogRepositoryTest.class);

    @Autowired
    private RequestResponseLogRepository requestResponseLogRepository;

    @Test
    public void testRequestResponseLog() {
        LOG.debug("****************** starting test");

        requestResponseLogRepository.deleteAll();

        RequestResponseLog rrlog = new RequestResponseLog();
        rrlog.setLogTimestamp(new Date());

        HttpStatus httpStatus = HttpStatus.OK;
        rrlog.setHttpStatus(httpStatus);
        MessageContent messageContent = new MessageContent();

        rrlog.setMessageContent(messageContent);

        AuthError authError = new AuthError();

        rrlog.setAuthError(authError);
        ErrorMessage errorMessage = new ErrorMessage();
        rrlog.setErrorMessage(errorMessage);

        rrlog = requestResponseLogRepository.save(rrlog);

        assertEquals(1, requestResponseLogRepository.count());

        Optional<RequestResponseLog> optional = requestResponseLogRepository.findById(rrlog.getId());
        RequestResponseLog foundLog = optional.get();

        LOG.debug("found user: " + foundLog);

        LOG.debug("****************** test complete");
    }

}
