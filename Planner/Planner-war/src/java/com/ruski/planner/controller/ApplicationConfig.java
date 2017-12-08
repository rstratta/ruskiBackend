/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.controller;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Rodrigo Stratta
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        configLogs();
        return resources;
    }


    private void configLogs() {
        PropertyConfigurator.configure("log4j.properties");  
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.ruski.planner.controller.PlansManagerResource.class);
    }
    
}
