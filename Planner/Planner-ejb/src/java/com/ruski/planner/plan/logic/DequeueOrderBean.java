/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.logic;

import com.ruski.mediator.plan.PlanDto;

/**
 *
 * @author Rodrigo Stratta
 */
public interface DequeueOrderBean {
    void addPlanFromOrderMessage(PlanDto planDto);
    
    void updatePlanFromOrderMessage(PlanDto planDto);
    
    void removePlanFromOrderMessage(PlanDto planDto);
}
