/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.logic;

import com.ruski.mediator.plan.PlanDto;
import java.util.List;

/**
 *
 * @author Rodrigo Stratta
 */
public interface PlanBean {
    void updatePlan(PlanDto planDto) throws PlanOperationException;

    void confirmPlan(String id) throws PlanOperationException, PlanValidationException;

    List<PlanDto> getPlansPendingConfirmation() throws PlanOperationException;

    
}
