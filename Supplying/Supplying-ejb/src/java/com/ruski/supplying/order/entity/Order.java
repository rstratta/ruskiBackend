/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.entity;


import com.ruski.repository.AbstractEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Luis
 */
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L; 
    
    
    @Column(nullable = false)
    private long clientId;
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFrom;
    
    @Column(nullable = false)
    private int volume;
    
    @Column(nullable = false)
    private int servicePointId;
    
    @Column(nullable = false)
    private int billingCloseday;
    
    @Column
    private boolean deleted;

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

    public void setVolume(int voulme) {
        this.volume = voulme;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += getId() != null ? getId().hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Order)) {
            return false;
        }
        Order other = (Order) object;
        if ((getId() == null && other.getId()!= null) || (getId() != null && !getId().equals(other.getId()))) {
            return false;
        }
        return true;
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
