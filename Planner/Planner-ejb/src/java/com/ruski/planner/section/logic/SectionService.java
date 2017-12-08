/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.ruski.mediator.section.SectionDto;
import java.util.List;

/**
 *
 * @author Rodrigo Stratta
 */
public interface SectionService {
    
    List<SectionDto> getSectionsByServicePointId(int pointId);
}
