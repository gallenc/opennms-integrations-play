/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.web;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opennms.poc.api.roadfaultapi.dao.RequestResponseLogRepository;
import org.opennms.poc.api.roadfaultapi.model.AuthError;
import org.opennms.poc.api.roadfaultapi.model.ErrorMessage;
import org.opennms.poc.api.roadfaultapi.model.MessageContent;
import org.opennms.poc.api.roadfaultapi.model.RequestResponseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cgallen
 */
@Component
public class MessageLoggerService {

    private static final Logger LOG = LogManager.getLogger(MessageLoggerService.class);

    @Autowired
    private SocketHandler socketHandler;

    @Autowired
    private RequestResponseLogRepository requestResponseLogRepository;

    // default username and password for application. Empty = not set
    private String username = "";

    private String password = "";

    private ErrorMessage testErrorMessage = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ErrorMessage getTestErrorMessage() {
        return testErrorMessage;
    }

    public void setTestErrorMessage(ErrorMessage testErrorMessage) {
        this.testErrorMessage = testErrorMessage;
    }

    @Transactional
    public void logmessage(MessageContent messageContent,
            HttpStatus httpStatus, AuthError authError, ErrorMessage errorMessage, Exception ex) {
        RequestResponseLog rrlog = new RequestResponseLog();
        try {
            // note reply will be same as request content message or error.
            rrlog.setLogTimestamp(new Date());
            rrlog.setHttpStatus(httpStatus);
            rrlog.setMessageContent(messageContent);
            rrlog.setAuthError(authError);
            rrlog.setErrorMessage(errorMessage);
            rrlog.setExceptionMessage((ex == null) ? null : ex.toString());

            rrlog = requestResponseLogRepository.save(rrlog);

            socketHandler.sendJsonTextMessage(rrlog);
        } catch (Exception e) {
            LOG.error("poblem logging message. ", e);
        }
        LOG.debug("logging message: " + rrlog.toString());
    }

}
