/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model;

import javax.persistence.Embeddable;

/**
 * Http Code 401 Content-Type application/json 
 * Body { "timestamp": 1586879649998,
 * "status": 401,
 * "error": "Unauthorized",
 * "message": "Bad credentials", "path":"/serviceHealth"
 *  }
 *
 * @author cgallen
 */
@Embeddable
public class AuthError {

    Long timestamp;
    Integer status;
    String error;
    String message;
    String path;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "AuthError{" + "timestamp=" + timestamp + ", status=" + status + ", error=" + error + ", message=" + message + ", path=" + path + '}';
    }
    
    
    
}
