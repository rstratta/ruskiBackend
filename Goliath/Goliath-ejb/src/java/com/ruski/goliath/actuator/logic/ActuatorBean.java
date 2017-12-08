/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.logic;

import java.util.List;
import javax.ejb.Local;
import com.ruski.mediator.actuator.ActuatorDto;
import com.ruski.mediator.actuator.CommandSequenceDto;

/**
 *
 * @author Luis
 */
@Local
public interface ActuatorBean {

    void addActuator(ActuatorDto orderToAdd) throws ActuatorValidationException, ActuatorOperationException;

    void executeProgram(List<CommandSequenceDto> commandsToExecute) throws ActuatorOperationException;
}
