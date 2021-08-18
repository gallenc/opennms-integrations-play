/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.opennms.poc.api.roadfaultapi.dao;


import org.opennms.poc.api.roadfaultapi.model.RequestResponseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cgallen
 */
@Repository
public interface RequestResponseLogRepository extends JpaRepository<RequestResponseLog,Long> {
    
    //public Page<RequestResponseLog> findAll(Long id, Pageable pageable);    
    
}
