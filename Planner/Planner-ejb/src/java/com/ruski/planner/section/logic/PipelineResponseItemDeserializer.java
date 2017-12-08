/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.logic;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ruski.mediator.section.SectionDto;
import java.lang.reflect.Type;

/**
 *
 * @author Rodrigo Stratta
 */
public class PipelineResponseItemDeserializer implements JsonDeserializer<SectionDto> {

    private Gson gson;
    
    public PipelineResponseItemDeserializer() {
        gson = new Gson();
    }
    
    @Override
    public SectionDto deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return gson.fromJson(json, SectionDto.class);
    }
    
}
