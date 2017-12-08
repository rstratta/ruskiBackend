/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.controllers;

import com.ruski.goliath.actuator.logic.ActuatorBean;
import com.ruski.goliath.actuator.logic.ActuatorOperationException;
import com.ruski.goliath.actuator.logic.ActuatorValidationException;
import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.actuator.ActuatorCommandsRequest;
import com.ruski.mediator.actuator.ActuatorDto;
import com.ruski.security.HashGenerationException;
import com.ruski.security.HashSecurityError;
import com.ruski.security.RuskiSecurityHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service
 *
 * @author Luis
 */
@Path("/actuators")
@RequestScoped
public class ActuatorsResource {

    private final Gson gson;

    @EJB(beanName = "GoliathActuatorBean")
    private ActuatorBean actuatorBean;

    @Context
    private UriInfo context;

    @EJB
    private RuskiLogger log4jRuskiLogger;
    
    private RuskiSecurityHandler securityHandler;
   
    public ActuatorsResource() {
        gson = new Gson();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createActuator(String actuator) {
        try {
            securityHandler = new RuskiSecurityHandler();
            log4jRuskiLogger.logInfo("Resource POST: addActuator");
            String jsonResult = securityHandler.decryptMessage("goliathFrontend", "goliath", actuator);
            ActuatorDto actuatorDto = gson.fromJson(jsonResult, ActuatorDto.class);
            actuatorBean.addActuator(actuatorDto);
            return Response.status(Response.Status.OK).entity("El actuador se creo correctamente").build();
        } catch (ActuatorOperationException e) {
            log4jRuskiLogger.logError("Error creating actuator" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (ActuatorValidationException | HashGenerationException | HashSecurityError e) {
            log4jRuskiLogger.logError("Error creating actuator" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/program")
    @Produces(MediaType.APPLICATION_JSON)
    public Response programPlan(String commandSequence) {
        try {
            securityHandler = new RuskiSecurityHandler();
            log4jRuskiLogger.logInfo("Resource POST: programPlan");
            String jsonResult = securityHandler.decryptMessage("goliathFrontend", "goliath", commandSequence);
            ActuatorCommandsRequest commands = gson.fromJson(jsonResult, ActuatorCommandsRequest.class);
            actuatorBean.executeProgram(commands.getCommands());
            return Response.status(Response.Status.CREATED).entity("El programa fue ejecutado correctamente").build();
        } catch (HashGenerationException | HashSecurityError e) {
            log4jRuskiLogger.logError("Error executing commands" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (ActuatorOperationException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
