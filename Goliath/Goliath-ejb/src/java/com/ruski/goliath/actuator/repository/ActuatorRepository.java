/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.repository;

import com.ruski.goliath.actuator.entity.Actuator;
import com.ruski.repository.RepositoryException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Luis Introini
 */
@Local
public interface ActuatorRepository {
    
    void addActuator(Actuator actuator) throws RepositoryException;   

    Actuator getActuatorById(String actuatorId) throws RepositoryException;
    
    List<Actuator> getAllActuators() throws RepositoryException;
    
    void updateActuator(Actuator actuator) throws RepositoryException;
    
}
