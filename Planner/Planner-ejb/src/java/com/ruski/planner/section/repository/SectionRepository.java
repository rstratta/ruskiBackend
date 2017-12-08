/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.repository;

import com.ruski.planner.section.entity.Section;
import com.ruski.repository.RepositoryException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Rodrigo Stratta
 */
@Local
public interface SectionRepository {
    
    void addSection(Section section)throws RepositoryException;
    
    void removeSection(String sectionId)throws RepositoryException;
    
    List<Section> getSectionsByPlanId(String planId)throws RepositoryException;
}
