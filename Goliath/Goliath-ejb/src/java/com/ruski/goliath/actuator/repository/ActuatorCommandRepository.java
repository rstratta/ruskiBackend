/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.repository;

import com.ruski.goliath.actuator.entity.Actuator;
import com.ruski.goliath.actuator.entity.ActuatorCommand;
import com.ruski.repository.RepositoryException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Luis Introini
 */
@Local
public interface ActuatorCommandRepository {
    
    void addActuatorCommand(ActuatorCommand command) throws RepositoryException;   

    ActuatorCommand getActuatorCommandById(String id) throws RepositoryException;
    
    List<ActuatorCommand> getCommandsFromActuator(Actuator actuator) throws RepositoryException;
    
}
