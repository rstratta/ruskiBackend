/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.logic;



/**
 *
 * @author Luis
 */
public class ActuatorOperationException extends Exception {
    
    public ActuatorOperationException(String message) {
        super(message);
    }

    public ActuatorOperationException(String message, Exception ex) {
        super(message+ ex.getMessage());
    }
}

