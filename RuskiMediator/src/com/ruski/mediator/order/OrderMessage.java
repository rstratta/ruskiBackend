/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.mediator.order;

import com.ruski.mediator.ActionEnum;
import java.io.Serializable;

/**
 *
 * @author Rodrigo Stratta
 */
public class OrderMessage implements Serializable {
    private static final long serialVersionUID = 1L; 
    private ActionEnum action;
    private OrderDto order;
    
    
    
    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderMessage{" + "action=" + action + ", order=" + order + '}';
    }
    
    
}
