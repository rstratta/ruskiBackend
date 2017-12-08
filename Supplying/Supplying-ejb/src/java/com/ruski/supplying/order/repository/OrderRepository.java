/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.repository;

import com.ruski.repository.RepositoryException;
import com.ruski.supplying.order.entity.Order;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Rodrigo Stratta
 */
@Local
public interface OrderRepository {
    
    void addOrder(Order order) throws RepositoryException;

    List<Order> getAllOrders() throws RepositoryException;

    void updateOrder(Order order) throws RepositoryException;

    Order getOrderById(String orderId) throws RepositoryException;
}
