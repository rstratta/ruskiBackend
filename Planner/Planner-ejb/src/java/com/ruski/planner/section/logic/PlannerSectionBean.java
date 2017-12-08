/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.plan.PlanDto;
import com.ruski.mediator.section.SectionDto;
import com.ruski.planner.plan.entity.Plan;
import com.ruski.planner.plan.logic.PlanBean;
import com.ruski.planner.plan.logic.PlanOperationException;
import com.ruski.planner.section.entity.Section;
import com.ruski.planner.section.repository.SectionRepository;
import com.ruski.repository.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Rodrigo Stratta
 */
@Stateless
public class PlannerSectionBean implements SectionBean {
    @EJB  private RuskiLogger log4jRuskiLogger;
    @EJB(beanName = "JpaSectionRepository")
    private SectionRepository sectionRepository;
    @EJB(beanName = "PipelineSectionService")
    private SectionService sectionService;
    @EJB(beanName = "PlannerPlanBean")
    private PlanBean planBean;
    
    @Override
    public void addSections(Plan plan) {
        log4jRuskiLogger.logInfo("Add Section for plan "  + plan);
        List<SectionDto> sections = getSectionsByServicePointId(plan.getServicePointId());
        saveSections(sections, plan);
        plan.setPlanState(Plan.PENDING_OF_APPROVATION);
        log4jRuskiLogger.logInfo("Update plan state " + Plan.PENDING_OF_APPROVATION);
        updatePlan(plan);
        
    }

    @Override
    public void removeSections(String planId) {
        try {
            log4jRuskiLogger.logInfo("Remove Sections from planId "  + planId);
            List<Section> sectionsToRemove = getSectionsFromRepositoryByPlanId(planId);
            for(Section section : sectionsToRemove){
                removeSectionById(section.getId());
            }
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error obtained sections" + ex.getMessage());
        }
        
    }

    @Override
    public List<SectionDto> getSectionsByServicePointId(int servicePoint) {
        return sectionService.getSectionsByServicePointId(servicePoint);
    }

    @Override
    public List<SectionDto> getSectionsByPlanId(String planId) {
        log4jRuskiLogger.logInfo("Start Getting sections by planId: "  + planId);
        List<SectionDto> result = new ArrayList<SectionDto>();
        try {
            List<Section> sections = getSectionsFromRepositoryByPlanId(planId);
            for(Section section : sections){
                result.add(buildSectionDto(section));
            }
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error obtained sections" + ex.getMessage());
        }
        return result;
    }

    @Override
    public void removeSectionById(String sectionId) {
        try {
            sectionRepository.removeSection(sectionId);
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error removing section" + ex.getMessage());
        }
    }

    private void saveSections(List<SectionDto> sections, Plan plan) {
        for(SectionDto dto: sections){
            Section section = buildSection(dto, plan);
            log4jRuskiLogger.logInfo("Save section: "  + section);
            saveSection(section);
        }
    }

    private Section buildSection(SectionDto dto, Plan plan) {
        Section section = new Section();
        section.setId(dto.getSectionId());
        section.setActuatorId(dto.getActuatorId());
        section.setSourceId(dto.getSourceId());
        section.setPlan(plan);
        return section;
    }

    private void saveSection(Section section) {
        try {
            sectionRepository.addSection(section);
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error save section:" + ex.getMessage());
        }
    }

    private List<Section> getSectionsFromRepositoryByPlanId(String planId) throws RepositoryException {
        return sectionRepository.getSectionsByPlanId(planId);
    }

    private SectionDto buildSectionDto(Section section) {
        SectionDto dto = new SectionDto();
        dto.setActuatorId(section.getActuatorId());
        dto.setSectionId(section.getId());
        dto.setSourceId(section.getSourceId());
        return dto;
    }

    private void updatePlan(Plan plan) {
        PlanDto dto = createPlanDto(plan);
        try {
            planBean.updatePlan(dto);
        } catch (PlanOperationException ex) {
            log4jRuskiLogger.logError("Error updating plan" + ex.getMessage());
        }
    }

    private PlanDto createPlanDto(Plan plan) {
        PlanDto dto = new PlanDto();
        dto.setClientId(plan.getClientId());
        dto.setPlanId(plan.getId());
        dto.setDeleted(plan.isDeleted());
        dto.setOrderDateFrom(plan.getOrderDateFrom());
        dto.setOrderId(plan.getOrderId());
        dto.setServicePointId(plan.getServicePointId());
        dto.setVolume(plan.getVolume());
        dto.setPlanState(plan.getPlanState());
        return dto;
    }
    
}
