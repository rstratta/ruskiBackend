/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.mediator.actuator;

import java.util.List;

/**
 *
 * @author Luis
 */
public class CommandSequenceDto {
    private String actuatorId;
    private List<Commands> commandasToExecute;

    public String getActuatorId() {
        return actuatorId;
    }

    public void setActuatorId(String actuatorId) {
        this.actuatorId = actuatorId;
    }

    public List<Commands> getCommandasToExecute() {
        return commandasToExecute;
    }

    public void setCommandasToExecute(List<Commands> commandasToExecute) {
        this.commandasToExecute = commandasToExecute;
    }        
}
