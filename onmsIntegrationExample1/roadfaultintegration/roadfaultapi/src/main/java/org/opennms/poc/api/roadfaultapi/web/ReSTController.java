/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.web;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opennms.poc.api.roadfaultapi.model.AuthError;
import org.opennms.poc.api.roadfaultapi.model.ErrorMessage;
import org.opennms.poc.api.roadfaultapi.model.MessageContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author cgallen
 */
@RestController
@RequestMapping("/")
public class ReSTController {

    private static final Logger LOG = LogManager.getLogger(ReSTController.class);

    @Autowired
    private MessageLoggerService messageLogger;

    /*
    serviceHealth
     */
    @RequestMapping(value = "/serviceHealth", method = {RequestMethod.POST},
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> sendMessage(@RequestBody(required = false) MessageContent[] messageArray,
            @RequestHeader Map<String, String> headers) {
        
        String authString = headers.get("authorization");

        // headers.forEach((key, value) -> {
        //   LOG.debug(String.format("Header '%s' = %s", key, value));
        //   });
        // LOG.debug("Auth String: "+authString);
        
        // check password if no username set then dont check password
        if (messageLogger.getUsername() != null && ! "".equals(messageLogger.getUsername())) {
            LOG.debug("checking authorisation for username: "+messageLogger.getUsername());
            if(authString==null) {
                throw new IllegalArgumentException("no authorization header set");
            } 
            String[] userPass = BasicAuthCoder.decode(authString);
            if ((!messageLogger.getUsername().equals(userPass[0]))
                    || !messageLogger.getPassword().equals(userPass[1])) {
                AuthError authError = new AuthError();
                //"status": 401,
                //"error": "Unauthorized",
                //"message": "Bad credentials", "path":"/serviceHealth"
                authError.setStatus(HttpStatus.UNAUTHORIZED.value());
                authError.setError(HttpStatus.UNAUTHORIZED.toString());
                authError.setMessage("Bad credentials");
                authError.setPath("/serviceHealth");
                authError.setTimestamp(new Date().getTime());
                messageLogger.logmessage(null, HttpStatus.UNAUTHORIZED, authError, null, null);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authError);
            }
        }

        LOG.debug("messages received:" + messageArray);
        if (messageArray == null || messageArray.length == 0) {
            // send error message if no messages in array
            ErrorMessage em = new ErrorMessage();
            em.setError_type("messageArray null or empty");
            em.setError_message("messageArray null or empty");

            messageLogger.logmessage(null, HttpStatus.BAD_REQUEST, null, em, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(em);
            
        } else if(messageLogger.getTestErrorMessage()!=null) {
            // send default error message
            ErrorMessage em = messageLogger.getTestErrorMessage();
            messageLogger.logmessage(null, HttpStatus.BAD_REQUEST, null, em, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(em);
        } else {
            for (MessageContent mcontent : messageArray) {
                // created == https status 201
                messageLogger.logmessage(mcontent, HttpStatus.CREATED, null, null, null);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(messageArray);
        }

    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleGenericException(Exception ex, WebRequest request
    ) {
        LOG.error("genericExceptionHandlerCalled for exception: ", ex);
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setError_message(ex.getMessage());
        errorMessage.setError_type("generic exception");

        messageLogger.logmessage(null, HttpStatus.BAD_REQUEST, null, errorMessage, ex);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
