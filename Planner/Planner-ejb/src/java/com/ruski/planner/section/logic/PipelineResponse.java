/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.ruski.mediator.section.SectionDto;
import java.util.Collection;


/**
 *
 * @author Rodrigo Stratta
 */
public class PipelineResponse {

    private Collection<SectionDto> sections;

    public Collection<SectionDto> getSections() {
        return sections;
    }

    public void setSections(Collection<SectionDto> sections) {
        this.sections = sections;
    }
}
