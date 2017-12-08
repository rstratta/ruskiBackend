/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.logic;

import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.order.OrderDto;
import com.ruski.mediator.order.OrderMessage;
import com.ruski.mediator.plan.PlanDto;
import com.ruski.security.HashGenerationException;
import com.ruski.security.HashSecurityError;
import com.ruski.security.RuskiSecurityHandler;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author Rodrigo Stratta
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/supplyingQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class OrderQueueListener implements MessageListener {
    @EJB private RuskiLogger log4jRuskiLogger;
    private Gson gson;
    @EJB(beanName = "PlannerPlanBean")
    private DequeueOrderBean dequeueOrderBean;
    
    public OrderQueueListener() {
        gson = new Gson();
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            String encodeMessage = ((TextMessage) message).getText();
            RuskiSecurityHandler securityHandler = new RuskiSecurityHandler();
            String originalJson = securityHandler.decryptMessage("supplying", "planner", encodeMessage);
            OrderMessage orderMessage = gson.fromJson(originalJson, OrderMessage.class);
            processOrderMessage(orderMessage);
            log4jRuskiLogger.logInfo("Dequeue order message " + orderMessage);
        } catch (JMSException ex) {
            log4jRuskiLogger.logError("Error reading message queue" + ex.getMessage());
        } catch (HashGenerationException ex) {
            log4jRuskiLogger.logError("Error creating RuskiSecurityHandler" + ex.getMessage());
        } catch (HashSecurityError ex) {
            log4jRuskiLogger.logError("Error decoding message" + ex.getMessage());
        }
        
    }

    private void processOrderMessage(OrderMessage orderMessage) {
        switch (orderMessage.getAction()) {
            case ADD_ACTION:
                dequeueOrderBean.addPlanFromOrderMessage(buildPlanDto(orderMessage.getOrder()));
                break;
            case EDIT_ACTION:
                dequeueOrderBean.updatePlanFromOrderMessage(buildPlanDto(orderMessage.getOrder()));
                break;
            case REMOVE_ACTION:
                dequeueOrderBean.removePlanFromOrderMessage(buildPlanDto(orderMessage.getOrder()));
                break;
            default:
                break;
        }
    }
    
    private PlanDto buildPlanDto(OrderDto order) {
        PlanDto dto = new PlanDto();
        dto.setClientId(order.getClientId());
        dto.setOrderId(order.getId());
        dto.setOrderDateFrom(order.getDateFrom());
        dto.setServicePointId(order.getServicePointId());
        dto.setVolume(order.getVolume());
        return dto;
    }
}