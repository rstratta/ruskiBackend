/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.mediator.actuator;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Luis
 */
public class ActuatorDto implements Serializable  {
    private static final long serialVersionUID = 1L; 
    private String id;
    private String name;
    private List<ActuatorCommandDto> commands;

    public ActuatorDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public ActuatorDto() {
    
    }
    
    public List<ActuatorCommandDto> getCommands() {
        return commands;
    }

    public void setCommands(List<ActuatorCommandDto> commands) {
        this.commands = commands;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ActuatorDto{" + "id=" + id + ", name=" + name + ", commands=" + commands + '}';
    }
    
    
    
}
