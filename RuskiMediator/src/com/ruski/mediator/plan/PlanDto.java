/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.mediator.plan;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Rodrigo Stratta
 */
public class PlanDto implements Serializable {
    private static final long serialVersionUID = 1L; 
    private String planId;
    private String orderId; 
    private long clientId;
    private Date orderDateFrom;
    private int volume;
    private int servicePointId;
    private boolean deleted;
    private String planState;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Date getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(Date orderDateFrom) {
        this.orderDateFrom = orderDateFrom;
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

    public void setPlanState(String planState) {
        this.planState = planState;
    }

    public String getPlanState() {
        return planState;
    }

    @Override
    public String toString() {
        return "PlanDto{" + "planId=" + planId + ", orderId="
                + orderId + ", clientId=" + clientId + ", orderDateFrom=" 
                + orderDateFrom + ", volume=" + volume + ", servicePointId=" 
                + servicePointId + ", deleted=" + deleted + ", planState=" + planState + '}';
    }
    
    
    
}
