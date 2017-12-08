/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.logic;

import com.ruski.mediator.order.OrderDto;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Rodrigo Stratta
 */
@Local
public interface OrderBean {
    void addOrder(OrderDto orderToAdd) throws OrderOperationException, OrderValidationException;
   
    void updateOrder(OrderDto dto) throws OrderOperationException, OrderValidationException;
    
    void deleteOrderById(String orderId) throws OrderValidationException, OrderOperationException;
    
    List<OrderDto> getOrders() throws OrderOperationException;
    
    OrderDto getOrderById(String orderId) throws OrderOperationException, OrderValidationException;
}
