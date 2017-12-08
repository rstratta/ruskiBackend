/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.entity;

import com.ruski.repository.AbstractEntity;
import com.ruski.planner.plan.entity.Plan;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Rodrigo Stratta
 */
@Entity
@Table(name = "sections")
public class Section extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L; 
    
    @ManyToOne
    @JoinColumn(name = "planId", nullable = false)
    private Plan plan;
    @Column(nullable = false)
    private String actuatorId;
    @Column(nullable = false)
    private String sourceId;

    public String getActuatorId() {
        return actuatorId;
    }

    public void setActuatorId(String actuatorId) {
        this.actuatorId = actuatorId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "Section{" + "plan=" + plan + ", actuatorId=" + actuatorId + ", sourceId=" + sourceId + '}';
    }
    
}
