/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.section.SectionDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rodrigo Stratta
 */
@Stateless
public class PipelineSectionService implements SectionService {
    private static final String URL_PROPERTY = "pipeline.service.url";
    @EJB  private RuskiLogger log4jRuskiLogger;
    private String urlService;
    private Gson gson;
    
    @PostConstruct
    public void init(){
        urlService = "https://pipeline-calculator-api.herokuapp.com/pipeline-route/service/";
        //(String) PropertyHandler.getPropertyValue(URL_PROPERTY);
        gson = new GsonBuilder().registerTypeAdapter(List.class, new PipelineResponseItemDeserializer()).create();
    }
    
    
    @Override
    public List<SectionDto> getSectionsByServicePointId(int pointId) {
        log4jRuskiLogger.logInfo("Invoke PipelineService");
        List<SectionDto> sectionsResult = new ArrayList<SectionDto>();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(urlService + pointId);
        String jsonResponse = target.request(MediaType.APPLICATION_JSON).post(Entity.json(null), String.class);
        sectionsResult.addAll(gson.fromJson(jsonResponse, PipelineResponse.class).getSections());
        return sectionsResult;
    }
}
