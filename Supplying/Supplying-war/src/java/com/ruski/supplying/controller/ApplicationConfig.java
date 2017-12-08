/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.controller;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Luis
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
    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.ruski.supplying.controller.OrdersResource.class);
    }
    
}
