/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.entity;

import com.ruski.repository.AbstractEntity;
import com.ruski.mediator.actuator.Commands;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Luis
 */
@Entity
@Table(name = "actuatorcommands")
public class ActuatorCommand extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L; 
    
    @ManyToOne
    @JoinColumn(name = "actuatorId", nullable = false)
    private Actuator actuator;

    @Enumerated(EnumType.STRING)
    private Commands command;
    
    @Column(nullable = false)                               
    private String jarToExecute;
    
    @Column(nullable = false)                               
    private String classToExecute;
        
    @Column(nullable = false)
    private String methodToExecute;
    
    @Column
    private String methodParam;

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public Commands getCommand() {
        return command;
    }

    public void setCommand(Commands command) {
        this.command = command;
    }

    public String getClassToExecute() {
        return classToExecute;
    }

    public void setClassToExecute(String classToExecute) {
        this.classToExecute = classToExecute;
    }

    public String getMethodToExecute() {
        return methodToExecute;
    }

    public void setMethodToExecute(String methodToExecute) {
        this.methodToExecute = methodToExecute;
    }

    public String getJarToExecute() {
        return jarToExecute;
    }

    public void setJarToExecute(String jarToExecute) {
        this.jarToExecute = jarToExecute;
    }

    public String getMethodParam() {
        return methodParam;
    }

    public void setMethodParam(String methodParam) {
        this.methodParam = methodParam;
    }
    
}
