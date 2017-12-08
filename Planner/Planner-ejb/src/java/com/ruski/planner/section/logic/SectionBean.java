/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.ruski.planner.plan.entity.Plan;
import com.ruski.mediator.section.SectionDto;
import java.util.List;

/**
 *
 * @author Rodrigo Stratta
 */
public interface SectionBean {
    
    void addSections(Plan plan);
    
    void removeSections(String planId);
    
    List<SectionDto> getSectionsByServicePointId(int servicePoint);
    
    List<SectionDto> getSectionsByPlanId(String planId);
    
    void removeSectionById(String sectionId);
}
