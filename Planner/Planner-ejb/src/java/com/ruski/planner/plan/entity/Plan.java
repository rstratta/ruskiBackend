/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.entity;

import com.ruski.planner.section.entity.Section;
import com.ruski.repository.AbstractEntity;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Rodrigo Stratta
 */
@Entity
@Table(name = "plans")
public class Plan extends AbstractEntity {
    public static final String PENDING_SECTIONS = "PS";
    public static final String PENDING_OF_APPROVATION = "PA";
    public static final String PENDING_OF_PROGRAMING = "PP";
    private static final long serialVersionUID = 1L; 
    
        
    @Column(nullable = false)
    private String orderId; 
    
    @Column(nullable = false)
    private long clientId;
    
    @Column(nullable = false)
    private String planState;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date orderDateFrom;
    
    @Column(nullable = false)
    private int volume;
    
    @Column(nullable = false)
    private int servicePointId;
    
    @Column
    private boolean deleted;
    
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "plan")
    private Set<Section> sections;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getServicePointId() {
        return servicePointId;
    }

    public void setServicePointId(int servicePointId) {
        this.servicePointId = servicePointId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(Date orderDateFrom) {
        this.orderDateFrom = orderDateFrom;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    public String getPlanState() {
        return planState;
    }

    public void setPlanState(String planState) {
        this.planState = planState;
    }

    @Override
    public String toString() {
        return "Plan{" + "orderId=" + orderId + ", clientId=" + clientId 
                + ", planState=" + planState + ", orderDateFrom=" + orderDateFrom 
                    + ", volume=" + volume + ", servicePointId=" + servicePointId 
                        + ", deleted=" + deleted + ", sections=" + sections + '}';
    }
    
}
