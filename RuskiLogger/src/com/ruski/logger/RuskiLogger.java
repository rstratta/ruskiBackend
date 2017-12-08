/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.logger;

import javax.ejb.Remote;

/**
 *
 * @author Rodrigo Stratta
 */
@Remote
public interface RuskiLogger {
    void logInfo(String messageInfo);
    
    void logError(String message);
    
    void logError(String message, Exception ex);
}
