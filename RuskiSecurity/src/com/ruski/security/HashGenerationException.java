/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.security;

/**
 *
 * @author Rodrigo Stratta
 */
public class HashGenerationException extends Exception {
    public static final long serialVersionUID = -1L;
    
    public HashGenerationException(String message) {
        super(message);
    }
    
    public HashGenerationException(String message, Exception ex) {
        super(message+ ex.getMessage());
    }
}
