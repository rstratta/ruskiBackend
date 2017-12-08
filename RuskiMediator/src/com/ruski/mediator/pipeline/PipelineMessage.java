/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.mediator.pipeline;

import com.ruski.mediator.ActionEnum;
import com.ruski.mediator.plan.PlanDto;
import java.io.Serializable;

/**
 *
 * @author Rodrigo Stratta
 */
public class PipelineMessage implements Serializable {
    private static final long serialVersionUID = 1L; 
    private ActionEnum action;
    private PlanDto plan;

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public PlanDto getPlan() {
        return plan;
    }

    public void setPlan(PlanDto plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "PipelineMessage{" + "action=" + action + ", plan=" + plan + '}';
    }    
}
