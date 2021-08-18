/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.web.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.opennms.poc.api.roadfaultapi.web.BasicAuthCoder;

/**
 *
 * @author cgallen
 */
public class BasicAuthEncoderTest {
    
    @Test
    public void testEncoderDecoder(){
        String username = "Craig";
        String password = "CraigPassword";
        String authPayload = BasicAuthCoder.encode(username, password);
        
        String[] credentials = BasicAuthCoder.decode(authPayload);
        assertEquals(username, credentials[0]);
        assertEquals(password, credentials[1]);
        
        // username="Aladdin" and password="open sesame"
        String TEST_AUTH = "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==";
        credentials = BasicAuthCoder.decode(TEST_AUTH);
        assertEquals("Aladdin", credentials[0]);
        assertEquals("open sesame", credentials[1]);
        
    }
}
