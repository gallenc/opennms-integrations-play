/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model;

/**
 *
 * @author cgallen
 */
public enum StatusValue {

    HEALTHY, // – The given status name has no issues

    FAULTY, // – The given status name has a fault with no further granularity available

    WARN, // – The given status name has a fault that should be treated as a warning

    CRITICAL //– The given status name has a fault that should be treated as critical

}
