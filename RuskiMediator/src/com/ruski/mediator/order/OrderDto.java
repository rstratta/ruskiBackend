/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.mediator.order;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Rodrigo Stratta
 */
public class OrderDto implements Serializable { 
    private static final long serialVersionUID = 1L; 
    private String id;
    private long clientId;
    private Date dateFrom;
    private int volume;
    private int servicePointId;
    private int billingCloseday;
    private boolean deleted;
    
    public OrderDto() { }

    public OrderDto(String id, long clientId, Date dateFrom, int servicePointId, 
            int billingCloseday, int volume, boolean deleted) {
        this.id = id;
        this.clientId = clientId;
        this.dateFrom = dateFrom;
        this.servicePointId = servicePointId;
        this.billingCloseday = billingCloseday;
        this.volume = volume;
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
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

    public int getBillingCloseday() {
        return billingCloseday;
    }

    public void setBillingCloseday(int billingCloseday) {
        this.billingCloseday = billingCloseday;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderId: ").append(getId()).append(" clientId: ").append(getClientId());
        builder.append(" dateFrom: ").append(getDateFrom()).append(" volume: ").append(getVolume());
        builder.append(" servicePoint: ").append(getServicePointId()).append(" billCloseDate: ");
        return builder.toString();
    }
}
