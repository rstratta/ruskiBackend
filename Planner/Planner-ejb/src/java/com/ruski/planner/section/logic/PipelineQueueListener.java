/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.ruski.planner.plan.entity.Plan;
import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.pipeline.PipelineMessage;
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
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/pipelineQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")})
public class PipelineQueueListener implements MessageListener {
    @EJB  private RuskiLogger log4jRuskiLogger;
    private Gson gson;
    @EJB(beanName = "PlannerSectionBean")
    private SectionBean sectionBean;
    
    public PipelineQueueListener() {
        gson = new Gson();
    }

    @Override
    public void onMessage(Message message) {
        try {
            String encodeMessage = ((TextMessage) message).getText();
            RuskiSecurityHandler securityHandler = new RuskiSecurityHandler();
            String originalJson = securityHandler.decryptMessage("planner", "pipeline", encodeMessage);
            PipelineMessage pipelineMessage = gson.fromJson(originalJson, PipelineMessage.class);
            processPipelineMessage(pipelineMessage);
            log4jRuskiLogger.logInfo("Dequeue PipelineMessage " + pipelineMessage);
        } catch (JMSException ex) {
            log4jRuskiLogger.logError("Error reading message queue" + ex.getMessage());
        } catch (HashGenerationException ex) {
            log4jRuskiLogger.logError("Error creating RuskiSecurityHandler" + ex.getMessage());
        } catch (HashSecurityError ex) {
            log4jRuskiLogger.logError("Error decoding message" + ex.getMessage());
        }
        
    }

    private void processPipelineMessage(PipelineMessage pipelineMessage) {
        switch (pipelineMessage.getAction()) {
            case ADD_ACTION:
                sectionBean.addSections(createPlamFromDto(pipelineMessage.getPlan()));
                break;
            case REMOVE_ACTION:
                sectionBean.removeSectionById(pipelineMessage.getPlan().getPlanId());
                break;
            default:
                break;
        }
    }
    
    private Plan createPlamFromDto(PlanDto dto) {
        Plan plan = new Plan();
        plan.setClientId(dto.getClientId());
        plan.setOrderDateFrom(dto.getOrderDateFrom());
        plan.setOrderId(dto.getOrderId());
        plan.setServicePointId(dto.getServicePointId());
        plan.setVolume(dto.getVolume());
        plan.setId(dto.getPlanId());
        return plan;
    }
    
}
