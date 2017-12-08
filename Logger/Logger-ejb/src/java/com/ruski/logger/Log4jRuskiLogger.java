/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.logger;

import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author Rodrigo Stratta
 */
@Stateless
public class Log4jRuskiLogger implements RuskiLogger {
    private static final Logger LOGGER = Logger.getLogger("ruskiLogger");

    @Override
    public void logInfo(String messageInfo) {
        LOGGER.info(messageInfo);
    }

    @Override
    public void logError(String message) {
        LOGGER.error(message);
    }

    @Override
    public void logError(String message, Exception ex) {
        LOGGER.error(message+ ex.getMessage());
    }
    
}
