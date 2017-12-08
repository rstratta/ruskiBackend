/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.logic;


import com.ruski.planner.plan.entity.Plan;
import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.ActionEnum;
import com.ruski.mediator.pipeline.PipelineMessage;
import com.ruski.mediator.plan.PlanDto;
import com.ruski.planner.plan.repository.PlanRepository;
import com.ruski.repository.RepositoryException;
import com.ruski.security.HashGenerationException;
import com.ruski.security.RuskiSecurityHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * @author Rodrigo Stratta
 */

@Stateless
public class PlannerPlanBean implements DequeueOrderBean, PlanBean {

    @EJB  private RuskiLogger log4jRuskiLogger;
    private Gson gson;
    @EJB(beanName = "JpaPlanRepository")
    private PlanRepository planRepository;
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/pipelineQueue")
    private Queue queue;

    @PostConstruct
    public void init(){
        gson = new Gson();
    }
    
    @Override
    public void addPlanFromOrderMessage(PlanDto planDto) {
        log4jRuskiLogger.logInfo("Add plan from message " + planDto);
        Plan plan = createPlamFromDto(planDto);
        plan.setId(UUID.randomUUID().toString());
        plan.setPlanState(Plan.PENDING_SECTIONS);
        try {
            planRepository.addPlan(plan);
            planDto.setPlanId(plan.getId());
            planDto.setPlanState(plan.getPlanState());
            planDto.setOrderDateFrom(plan.getOrderDateFrom());
            addSections(planDto);
            log4jRuskiLogger.logInfo("Finish add plan");
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error with planRepository" + ex.getMessage());
        }
    }

    @Override
    public void updatePlanFromOrderMessage(PlanDto planDto) {
        try {
            log4jRuskiLogger.logInfo("Update plan from message " + planDto);
            Plan plan = planRepository.getPlanByOrderId(planDto.getOrderId());
            planDto.setPlanId(plan.getId());
            planDto.setPlanState(plan.getPlanState());
            updatePlan(planDto);
            updateSections(plan, planDto);
            log4jRuskiLogger.logInfo("Finish update plan from message");
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error with planRepository" + ex.getMessage());
        } catch (PlanOperationException ex) {
            log4jRuskiLogger.logError("Error updating plan" + ex.getMessage());
        }
    }

    @Override
    public void removePlanFromOrderMessage(PlanDto planDto) {
        try {
            log4jRuskiLogger.logInfo("Remove plan from message " + planDto);
            Plan plan = planRepository.getPlanByOrderId(planDto.getOrderId());
            Plan planToUpdate = createPlamFromDto(planDto);
            planToUpdate.setId(plan.getId());
            planToUpdate.setPlanState(plan.getPlanState());
            planToUpdate.setDeleted(true);
            planRepository.updatePlan(planToUpdate);
            log4jRuskiLogger.logInfo("Finish remove plan from message");
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error with planRepository" + ex.getMessage());
        }
    }
    
    @Override
    public void confirmPlan(String id) throws PlanOperationException, PlanValidationException {
        log4jRuskiLogger.logInfo("Confirm plan id: " + id);
        if (StringUtils.isEmpty(id)) {
            throw new PlanValidationException("Debe indicar un Id de plan a confirmar");
        }
        try {
            Plan planToConfirm = planRepository.getPlanById(id);
            planToConfirm.setPlanState(Plan.PENDING_OF_PROGRAMING);
            updatePlanInRepository(planToConfirm);
            log4jRuskiLogger.logInfo("Finish confirm plan");
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error getting plans" + ex.getMessage());
            throw new PlanOperationException("Error confirming plan", ex);
        }
    }

    @Override
    public List<PlanDto> getPlansPendingConfirmation() throws PlanOperationException {
        try {
            log4jRuskiLogger.logInfo("Starting get plans pending confirmation");
            List<Plan> plans = planRepository.getPendingsOfConfirmation();
            List<PlanDto> result = buildPlanDtoResult(plans);
            log4jRuskiLogger.logInfo("Finish get plans pending confirmation");
            return result;
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error getting plans pending confirmation" + ex.getMessage());
            throw new PlanOperationException("Error getting plans pending confirmation", ex);
        }
    }

    private Plan createPlamFromDto(PlanDto dto) {
        Plan plan = new Plan();
        plan.setClientId(dto.getClientId());
        plan.setOrderDateFrom(dto.getOrderDateFrom());
        plan.setOrderId(dto.getOrderId());
        plan.setServicePointId(dto.getServicePointId());
        plan.setVolume(dto.getVolume());
        plan.setPlanState(dto.getPlanState());
        return plan;
    }
    
   

    private void addSections(PlanDto plan) {
        buildMessage(ActionEnum.ADD_ACTION, plan );
    }

    private void updateSections(Plan plan, PlanDto planDto) {
        if (plan.getServicePointId() != planDto.getServicePointId()) {
            removeSections(planDto);
            addSections(planDto);
        }

    }

    private void removeSections(PlanDto planDto) {
        buildMessage(ActionEnum.REMOVE_ACTION, planDto);
    }
    
    private void buildMessage(ActionEnum actionEnum, PlanDto dto) {
        PipelineMessage message = new PipelineMessage();
        message.setPlan(dto);
        message.setAction(actionEnum);
        sendNotification(message);
    }
    
    private void sendNotification(PipelineMessage message) {
        try {
            log4jRuskiLogger.logInfo("Enqueue pipelineMessage " + message);
            RuskiSecurityHandler securityHandler = new RuskiSecurityHandler();
            String encodeJson = securityHandler.encryptMessage("planner", "pipeline", gson.toJson(message));
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            TextMessage txt = session.createTextMessage(encodeJson);
            MessageProducer producer = session.createProducer(queue);
            producer.send(txt);
            session.close();
            connection.close();
        } catch (JMSException e) {
            log4jRuskiLogger.logError("Error sending message" + e.getMessage());
        } catch (HashGenerationException ex) {
            log4jRuskiLogger.logError("Error creating RuskiSecurityHandler" + ex.getMessage());
        } 
    }

    @Override
    public void updatePlan(PlanDto planDto) throws PlanOperationException {
        try {
            log4jRuskiLogger.logInfo("Start update plan " + planDto);
            validatePlanDto(planDto);
            Plan planToUpdate = createPlamFromDto(planDto);
            planToUpdate.setId(planDto.getPlanId());
            updatePlanInRepository(planToUpdate);
            log4jRuskiLogger.logInfo("Finish update plan");
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error updating plan" + ex.getMessage());
            throw new PlanOperationException("Error updating plan", ex);
        } catch (PlanValidationException ex) {
            log4jRuskiLogger.logError("Error validating plan" + ex.getMessage());
            throw new PlanOperationException("Error validating plan", ex);
        }
    }

    private void validatePlanDto(PlanDto dto) throws PlanValidationException {
        if (StringUtils.isEmpty(dto.getPlanId())) {
            throw new PlanValidationException("Verifique el dia de facturación ingresado");
        } else if (dto.getServicePointId() <= 0) {
            throw new PlanValidationException("Verifique el punto de servicio ingresado");
        } else if (dto.getVolume() <= 0) {
            throw new PlanValidationException("Verifique el volumen ingresado");
        } else if (dto.getClientId() <= 0) {
            throw new PlanValidationException("Verifique el cliente ingresado");
        }
    }

    private void updatePlanInRepository(Plan plan) throws RepositoryException {
        planRepository.updatePlan(plan);
    }

    private List<PlanDto> buildPlanDtoResult(List<Plan> plans) {
        List<PlanDto> result = new ArrayList();
        for (Plan plan : plans){
            result.add(buildPlanDtoFromPlan(plan));
        }
        return result;
    }

    private PlanDto buildPlanDtoFromPlan(Plan plan) {
        PlanDto planDto = new PlanDto();
        planDto.setClientId(plan.getClientId());
        planDto.setOrderDateFrom(plan.getOrderDateFrom());
        planDto.setOrderId(plan.getOrderId());
        planDto.setServicePointId(plan.getServicePointId());
        planDto.setVolume(plan.getVolume());
        planDto.setPlanState(plan.getPlanState());
        planDto.setPlanId(plan.getId());
        return planDto;
    }

    
}
