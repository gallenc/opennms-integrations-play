/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import org.opennms.poc.api.roadfaultapi.dao.ListToJsonConverter;

/**
 *
 * {
 * "source": "APM-Checkmk", "equipmentClass": "u_server_cluster", "equipmentReference": "openshift12345", "statusTime": "2020-03-26T10:15:40.857Z",
 * "statusInformation": [ { "statusName": "AV Program Status", "statusValue": "WARN", "statusAdditionalInfo": "" } ] }
 *
 * @author cgallen
 */
@Embeddable
public class MessageContent {

    String source;
    String equipmentClass;
    String equipmentReference;
    List<StatusInformation> statusInformation;
    Date statusTime;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEquipmentClass() {
        return equipmentClass;
    }

    public void setEquipmentClass(String equipmentClass) {
        this.equipmentClass = equipmentClass;
    }

    public String getEquipmentReference() {
        return equipmentReference;
    }

    public void setEquipmentReference(String equipmentReference) {
        this.equipmentReference = equipmentReference;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    @Column(name = "statuslist")
    @Convert(converter = ListToJsonConverter.class)
    public List<StatusInformation> getStatusInformation() {
        return statusInformation;
    }

    public void setStatusInformation(List<StatusInformation> statusInformation) {
        this.statusInformation = statusInformation;
    }

    @Override
    public String toString() {
        return "MessageContent{" + "source=" + source + ", equipmentClass=" + equipmentClass + ", equipmentReference=" + equipmentReference + ", statusInformation=" + statusInformation + ", statusTime=" + statusTime + '}';
    }

}
