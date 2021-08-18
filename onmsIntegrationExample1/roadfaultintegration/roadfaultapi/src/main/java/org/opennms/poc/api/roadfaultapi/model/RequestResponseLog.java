/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.http.HttpStatus;

/**
 *
 * @author cgallen
 */
@Entity
public class RequestResponseLog {

    Long id;
    Date logTimestamp = new Date();
    MessageContent messageContent=null;
    HttpStatus httpStatus=null;
    AuthError authError=null;
    ErrorMessage errorMessage=null;
    String exceptionMessage=null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLogTimestamp() {
        return logTimestamp;
    }

    public void setLogTimestamp(Date logTimestamp) {
        this.logTimestamp = logTimestamp;
    }


    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public AuthError getAuthError() {
        return authError;
    }

    public void setAuthError(AuthError authError) {
        this.authError = authError;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public String toString() {
        return "RequestResponseLog{" + "id=" + id + ", logTimestamp=" + logTimestamp + ", messageContent=" + messageContent + ", httpStatus=" + httpStatus + ", authError=" + authError + ", errorMessage=" + errorMessage + ", exceptionMessage=" + exceptionMessage + '}';
    }

 



}
