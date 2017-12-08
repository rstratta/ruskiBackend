/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.controller;

import com.ruski.planner.plan.logic.PlanBean;
import com.ruski.planner.plan.logic.PlanValidationException;
import com.ruski.planner.plan.logic.PlanOperationException;
import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.plan.PlanDto;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Rodrigo Stratta
 */
@Path("plansManager")
@RequestScoped
public class PlansManagerResource {
    @EJB private RuskiLogger log4jRuskiLogger;
    private final Gson gson;
    @EJB
    private PlanBean planBean;
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PlansManagerResource
     */
    public PlansManagerResource() {
        gson = new Gson();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmPlans(@PathParam("id") String id) {
        try {
            log4jRuskiLogger.logInfo("Resource PUT: confirmPlan id: " + id);
            planBean.confirmPlan(id);
            return Response.status(Response.Status.OK).entity("El plan ha sido confirmado").build();
        } catch (PlanOperationException e) {
            log4jRuskiLogger.logError("Error on confirm plan" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (PlanValidationException e) {
            log4jRuskiLogger.logError("Error on confirm plan" + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @GET    
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSections() {
        try {
            log4jRuskiLogger.logInfo("Resource GET: get sections: ");
            List<PlanDto> plans = planBean.getPlansPendingConfirmation();
            return Response.status(Response.Status.OK).entity(gson.toJson(plans)).build();
        } catch (PlanOperationException e) {
            log4jRuskiLogger.logError("Error getting sections" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }   
}
