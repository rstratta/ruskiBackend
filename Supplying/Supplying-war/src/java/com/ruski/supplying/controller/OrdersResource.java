/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.controller;

import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.order.OrderDto;
import com.ruski.security.HashGenerationException;
import com.ruski.security.HashSecurityError;
import com.ruski.security.RuskiSecurityHandler;
import com.ruski.supplying.order.logic.OrderBean;
import com.ruski.supplying.order.logic.OrderOperationException;
import com.ruski.supplying.order.logic.OrderValidationException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("/orders")
public class OrdersResource {

    private final Gson gson;

    @EJB(beanName = "SupplyingOrderBean")
    private OrderBean orderBean;

    @Context
    private UriInfo context;

    @EJB
    private RuskiLogger log4jRuskiLogger;

    private RuskiSecurityHandler securityHandler;

    public OrdersResource() {
        gson = new Gson();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") String id) {
        try {
            log4jRuskiLogger.logInfo("Resource GET: getOrderById " + id);
            OrderDto result = orderBean.getOrderById(id);
            return Response.status(Response.Status.OK).entity(gson.toJson(result)).build();
        } catch (OrderOperationException e) {
            log4jRuskiLogger.logError("Error getting order by id." + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (OrderValidationException e) {
            log4jRuskiLogger.logError("Error getting order by id." + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() {
        try {
            log4jRuskiLogger.logInfo("Resource GET: getOrders");
            List<OrderDto> orders = orderBean.getOrders();
            return Response.status(Response.Status.OK).entity(gson.toJson(orders)).build();
        } catch (OrderOperationException e) {
            log4jRuskiLogger.logError("Error getting orders." + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(String order) {
        try {
            securityHandler = new RuskiSecurityHandler();
            log4jRuskiLogger.logInfo("Resource POST: createOrder");
            String jsonResult = securityHandler.decryptMessage("supplyingFrontend", "supplying", order);
            OrderDto orderDto = gson.fromJson(jsonResult, OrderDto.class);
            orderBean.addOrder(orderDto);
            return Response.status(Response.Status.CREATED).entity("La orden se creo correctamente").build();
        } catch (OrderOperationException e) {
            log4jRuskiLogger.logError("Error create order." + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (OrderValidationException e) {
            log4jRuskiLogger.logError("Error create order." + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (HashGenerationException ex) {
            log4jRuskiLogger.logError("Error create order." + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (HashSecurityError ex) {
            log4jRuskiLogger.logError("Error create order." + ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrder(String order) {
        try {
            securityHandler = new RuskiSecurityHandler();
            log4jRuskiLogger.logInfo("Resource PUT: updateOrder");
            String jsonResult = securityHandler.decryptMessage("supplyingFrontend", "supplying", order);
            OrderDto orderDto = gson.fromJson(jsonResult, OrderDto.class);
            orderBean.updateOrder(orderDto);
            return Response.status(Response.Status.OK).build();
        } catch (OrderOperationException e) {
            log4jRuskiLogger.logError("Error update order." + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (OrderValidationException e) {
            log4jRuskiLogger.logError("Error update order." + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (HashGenerationException e) {
            log4jRuskiLogger.logError("Error update order." + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (HashSecurityError e) {
            log4jRuskiLogger.logError("Error update order." + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrder(@PathParam("id") String id) {
        try {
            log4jRuskiLogger.logInfo("Resource DELTE: deleteOrder");
            orderBean.deleteOrderById(id);
            return Response.status(Response.Status.OK).entity("La orden se elimino correctamente").build();
        } catch (OrderOperationException e) {
            log4jRuskiLogger.logError("Error delete order." + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (OrderValidationException e) {
            log4jRuskiLogger.logError("Error delete order." + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
