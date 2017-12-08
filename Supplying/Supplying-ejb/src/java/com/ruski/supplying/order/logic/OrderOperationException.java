/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.logic;

import com.ruski.repository.RepositoryException;

/**
 *
 * @author Rodrigo Stratta
 */
public class OrderOperationException extends Exception {
    
    public OrderOperationException(String message) {
        super(message);
    }

    public OrderOperationException(String message, RepositoryException ex) {
        super(message+ ex.getMessage());
    }
}
