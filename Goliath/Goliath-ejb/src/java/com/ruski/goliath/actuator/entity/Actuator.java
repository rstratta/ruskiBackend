/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.entity;

import com.ruski.repository.AbstractEntity;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Luis
 */
@Entity
@Table(name = "actuators")
public class Actuator extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Column(nullable = false)
    private String name;
    
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "actuator")
    private Set<ActuatorCommand> commands;

    public String getName() {
        return name;
    }

    public Set<ActuatorCommand> getCommands() {
        return commands;
    }

    public void setCommands(Set<ActuatorCommand> commands) {
        this.commands = commands;
    }

    public void setName(String name) {
        this.name = name;
    }    
}
