/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model;

import javax.persistence.Embeddable;

/**
 * Http Code 400
 * Content-Type application/json
 * Body {
 * "error_message": "The type of error that has been reported”
 * "error_type": "Message detailing the error"
 * }
 * 
 * @author cgallen
 */
@Embeddable
public class ErrorMessage {
    
    String error_message;
            
    String error_type;

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError_type() {
        return error_type;
    }

    public void setError_type(String error_type) {
        this.error_type = error_type;
    }

    @Override
    public String toString() {
        return "Error{" + "error_message=" + error_message + ", error_type=" + error_type + '}';
    }
    
    
    
}
