/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.logic;

import com.ruski.supplying.order.repository.OrderRepository;
import com.google.gson.Gson;
import com.ruski.logger.RuskiLogger;
import com.ruski.mediator.ActionEnum;
import com.ruski.mediator.order.OrderDto;
import com.ruski.mediator.order.OrderMessage;
import com.ruski.repository.RepositoryException;
import com.ruski.security.HashGenerationException;
import com.ruski.security.RuskiSecurityHandler;
import com.ruski.supplying.order.entity.Order;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Luis
 */
@Stateless
public class SupplyingOrderBean implements OrderBean {

    @EJB  private RuskiLogger log4jRuskiLogger;
    private final Gson gson;
    @EJB(beanName = "JpaOrderRepository")
    private OrderRepository orderRepository;
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/supplyingTopic")
    private Topic topic;
    
    
    public SupplyingOrderBean() {
        gson = new Gson();
    }

    @PostConstruct
    private void init() {
         
    }

    @Override
    public void addOrder(OrderDto orderToAdd) throws OrderOperationException, OrderValidationException {
        log4jRuskiLogger.logInfo("Starting Add order: " + orderToAdd);
        validateDto(orderToAdd);
        Order order = createOrderFromDto(orderToAdd);
        order.setId(UUID.randomUUID().toString());
        order.setDateFrom(new Date(System.currentTimeMillis()));
        try {
            orderRepository.addOrder(order);
            buildMessage(ActionEnum.ADD_ACTION, buildFullOrderDto(order));
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error saving order" + ex.getMessage());
            throw new OrderOperationException("Error with repository", ex);
        }
        log4jRuskiLogger.logInfo("Finish add order: " + orderToAdd);
    }

    @Override
    public void updateOrder(OrderDto dto) throws OrderOperationException, OrderValidationException {
        log4jRuskiLogger.logInfo("Starting update order: " + dto);
        validateOrderId(dto.getId());
        validateDto(dto);
        Order order = createOrderFromDto(dto);
        order.setId(dto.getId());
        order.setDateFrom(dto.getDateFrom());
        try {
            orderRepository.updateOrder(order);
            buildMessage(ActionEnum.EDIT_ACTION, buildFullOrderDto(order));
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error saving order" + ex.getMessage());
            throw new OrderOperationException("Error with repository", ex);
        }
        log4jRuskiLogger.logInfo("Finish update order: " + dto);
    }

    private Order createOrderFromDto(OrderDto dto) {
        Order order = new Order();
        order.setBillingCloseday(dto.getBillingCloseday());
        order.setClientId(dto.getClientId());
        order.setServicePointId(dto.getServicePointId());
        order.setVolume(dto.getVolume());
        return order;
    }

    @Override
    public void deleteOrderById(String orderId) throws OrderValidationException, OrderOperationException {
        log4jRuskiLogger.logInfo("Starting remove orderId: " + orderId);
        validateOrderId(orderId);
        try {
            Order orderResult = orderRepository.getOrderById(orderId);
            orderResult.setDeleted(true);
            orderRepository.updateOrder(orderResult);
            buildMessage(ActionEnum.REMOVE_ACTION, buildFullOrderDto(orderResult));
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error remove orderId: " + orderId + ex.getMessage());
            throw new OrderOperationException("Error with repository", ex);
        }
        log4jRuskiLogger.logInfo("Finish remove orderId: " + orderId);
    }

    @Override
    public List<OrderDto> getOrders() throws OrderOperationException {
        try {
            log4jRuskiLogger.logInfo("Starting Getting all orders");
            List<Order> ordersResult = orderRepository.getAllOrders();
            List<OrderDto> result = buildOrderListResult(ordersResult);
            log4jRuskiLogger.logInfo("Finish Getting all orders");
            return result;
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error Getting all orders" + ex.getMessage());
            throw new OrderOperationException("Error with repository" + ex.getMessage());
        }
    }

    @Override
    public OrderDto getOrderById(String orderId) throws OrderOperationException, OrderValidationException {
        try {
            log4jRuskiLogger.logInfo("Starting Getting order by Id: " + orderId);
            validateOrderId(orderId);
            Order orderResult = orderRepository.getOrderById(orderId);
            OrderDto dto = buildOrderDto(orderResult);
            log4jRuskiLogger.logInfo("Finish Getting order by Id: " + orderId);
            return dto;
        } catch (RepositoryException ex) {
            log4jRuskiLogger.logError("Error Getting order by Id" + ex.getMessage());
            throw new OrderOperationException("Error with repository", ex);
        }
    }

    private void validateDto(OrderDto dto) throws OrderValidationException {
        if (dto.getBillingCloseday() < 1 || dto.getBillingCloseday() > 31) {
            throw new OrderValidationException("Verifique el dia de facturación ingresado");
        } else if (dto.getServicePointId() <= 0) {
            throw new OrderValidationException("Verifique el punto de servicio ingresado");
        } else if (dto.getVolume() <= 0) {
            throw new OrderValidationException("Verifique el volumen ingresado");
        } else if (dto.getClientId() <= 0) {
            throw new OrderValidationException("Verifique el cliente ingresado");
        }
    }

    private List<OrderDto> buildOrderListResult(List<Order> ordersResult) {
        List<OrderDto> orders = new ArrayList<>();
        for (Order order : ordersResult) {
            orders.add(buildOrderDto(order));
        }
        return orders;
    }

    private OrderDto buildOrderDto(Order order) {
        return new OrderDto(order.getId(), order.getClientId(), order.getDateFrom(), 
                order.getServicePointId(), order.getBillingCloseday(), order.getVolume(), order.isDeleted());
    }

    private void validateOrderId(String orderId) throws OrderValidationException {
        if (StringUtils.isEmpty(orderId)) {
            throw new OrderValidationException("Debe indicar un id de orden");
        }
    }

    private void sendNotification(OrderMessage message) {
        try {
            RuskiSecurityHandler securityHandler = new RuskiSecurityHandler();
            String encryptedMessage = securityHandler.encryptMessage("supplying", "supplying", gson.toJson(message));
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            TextMessage txt = session.createTextMessage(encryptedMessage);
            MessageProducer producer = session.createProducer(topic);
            producer.send(txt);
            session.close();
            connection.close();
        } catch (JMSException e) {
            log4jRuskiLogger.logError("Error sending topic" + e.getMessage());
        } catch (HashGenerationException e) {
            log4jRuskiLogger.logError("Error encoding topic" + e.getMessage());
        }
    }

    private void buildMessage(ActionEnum actionEnum, OrderDto dto) {
        OrderMessage message = new OrderMessage();
        message.setOrder(dto);
        message.setAction(actionEnum);
        sendNotification(message);
    }

    private OrderDto buildFullOrderDto(Order order) {
        OrderDto dto = buildOrderDto(order);
        dto.setId(order.getId());
        dto.setDateFrom(order.getDateFrom());
        dto.setDeleted(order.isDeleted());
        return dto;
    }
}
