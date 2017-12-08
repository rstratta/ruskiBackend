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
public class ActuatorCommandsRequest {
    private List<CommandSequenceDto> commands;

    public List<CommandSequenceDto> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandSequenceDto> commands) {
        this.commands = commands;
    }
    
}
