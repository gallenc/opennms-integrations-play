/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.web;


import org.opennms.poc.api.roadfaultapi.dao.PersistenceJPAConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;


/**
 *
 * @author cgallen
 */
@Configuration
@Import(PersistenceJPAConfig.class)
@PropertySource("classpath:persistence-app.properties")
public class DAOConfiguration {
    
}
