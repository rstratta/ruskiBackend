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
public class HashSecurityError extends Exception {
    public static final long serialVersionUID = -1L;
    
    public HashSecurityError(String message) {
        super(message);
    }
    
}
