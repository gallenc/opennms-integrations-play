/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model;

import javax.persistence.Embeddable;

/**
 *         "statusInformation": [
 *          {
 *              "statusName": "AV Program Status",
 *              "statusValue": "WARN",
 *              "statusAdditionalInfo": ""
 *          }
 * @author cgallen
 */
@Embeddable
public class StatusInformation {
    
    String statusName;
    StatusValue statusValue;
    String statusAdditionalInfo;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public StatusValue getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(StatusValue statusValue) {
        this.statusValue = statusValue;
    }

    public String getStatusAdditionalInfo() {
        return statusAdditionalInfo;
    }

    public void setStatusAdditionalInfo(String statusAdditionalInfo) {
        this.statusAdditionalInfo = statusAdditionalInfo;
    }

    @Override
    public String toString() {
        return "StatusInformation{" + "statusName=" + statusName + ", statusValue=" + statusValue + ", statusAdditionalInfo=" + statusAdditionalInfo + '}';
    }


}
