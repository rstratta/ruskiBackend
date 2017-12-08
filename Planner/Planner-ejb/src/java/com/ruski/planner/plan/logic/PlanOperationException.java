/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.logic;

/**
 *
 * @author Rodrigo Stratta
 */
public class PlanOperationException extends Exception {

    public PlanOperationException(String message, Exception ex) {
        super(message+ ex.getMessage());
    }
    
}
