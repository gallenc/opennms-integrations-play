/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.opennms.poc.api.roadfaultapi.model.AuthError;
import org.opennms.poc.api.roadfaultapi.model.MessageContent;
import org.opennms.poc.api.roadfaultapi.model.StatusInformation;
import org.opennms.poc.api.roadfaultapi.model.StatusValue;
import org.opennms.poc.api.roadfaultapi.model.ErrorMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author cgallen
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CreateMessageTest {

    /*
    Body [
{
"source":"APM-Checkmk",
"equipmentClass":"u_server_cluster",
"equipmentReference":"openshift12345",
"statusTime":"2020-03-26T10:15:40.857Z",
"statusInformation":[
{
"statusName":"AV Program Status",
"statusValue":"WARN",
"statusAdditionalInfo":""
}
]
}
     */
    @Test
    public void createMessageList() throws JsonProcessingException {

        // set up date formatter
        // https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date testDate;
        try {
            String DATE_EXAMPLE = "2020-03-26T10:15:40.857Z";
            System.out.println("example date Date is: " + DATE_EXAMPLE);
            testDate = formatter.parse(DATE_EXAMPLE);
            System.out.println("parsed input date Date is: " + testDate);
            String dstr = formatter.format(testDate);
            System.out.println("output date Date is: " + dstr);
            assertTrue(DATE_EXAMPLE.equals(dstr));
        } catch (ParseException e) {
            throw new RuntimeException("problem parsing date", e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.findAndRegisterModules()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        List<MessageContent> messageList = new ArrayList<MessageContent>();

        MessageContent message1 = new MessageContent();
        message1.setSource("APM-Checkmk");
        message1.setEquipmentClass("u_server_cluster");
        message1.setEquipmentReference("openshift12345");
        // "2020-03-26T10:15:40.857Z",
        message1.setStatusTime(testDate);

        StatusInformation statusInformation = new StatusInformation();
        statusInformation.setStatusName("AV Program Status");
        statusInformation.setStatusAdditionalInfo("some additional info");
        statusInformation.setStatusValue(StatusValue.FAULTY);
        
        List<StatusInformation> statusInformationList = Arrays.asList(statusInformation);

        message1.setStatusInformation(statusInformationList);

        messageList.add(message1);

        String messageListStr = objectMapper.writeValueAsString(messageList);

        System.out.println("Json output:\n" + messageListStr);

        // reading in the list
        CollectionType javaType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, MessageContent.class);
        List<MessageContent> receivedList = objectMapper.readValue(messageListStr, javaType);

        assertEquals(receivedList.get(0).toString(), message1.toString());

    }

    @Test
    public void createErrorMessageTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setError_message("Message detailing the error\"");
        errorMessage.setError_type("The type of error that has been reported”");

        String messageListStr = objectMapper.writeValueAsString(errorMessage);

        System.out.println("Json output:\n" + messageListStr);

        ErrorMessage receivedErrorMessage = objectMapper.readValue(messageListStr, ErrorMessage.class);

        assertEquals(errorMessage.toString(), receivedErrorMessage.toString());

    }

    @Test
    public void createAuthAuthErrorTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        AuthError authError = new AuthError();
        authError.setError("Unauthorized");
        authError.setMessage("Bad credentials");
        authError.setPath("/serviceHealth");
        authError.setStatus(401);
        authError.setTimestamp(1586879649998L);

        String authErrorString = objectMapper.writeValueAsString(authError);

        System.out.println("Json output:\n" + authErrorString);

        AuthError receivedAuthError = objectMapper.readValue(authErrorString, AuthError.class);

        assertEquals(authError.toString(), receivedAuthError.toString());

    }
}
