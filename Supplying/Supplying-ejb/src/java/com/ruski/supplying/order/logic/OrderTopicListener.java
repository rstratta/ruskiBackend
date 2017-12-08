/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.logic;

import com.ruski.logger.RuskiLogger;
import com.ruski.security.HashGenerationException;
import com.ruski.security.HashSecurityError;
import com.ruski.security.RuskiSecurityHandler;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author Rodrigo Stratta
 */

@MessageDriven(mappedName = "jms/supplyingTopic", activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/supplyingTopic"),
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/supplyingTopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")})

public class OrderTopicListener implements MessageListener {
    @EJB  private RuskiLogger log4jRuskiLogger;
    @Resource(lookup = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup = "jms/supplyingQueue")
    private Queue queue;
    
    @Override
    public void onMessage(Message message) {
        log4jRuskiLogger.logInfo("Receive order topic and enqueue message.");
        TextMessage txtMessage = (TextMessage) message;
        try {
            enqueueOrder(txtMessage.getText());
        } catch (JMSException ex) {
            log4jRuskiLogger.logError("Error when enqueue order" + ex.getMessage());
        }
    }

    private void enqueueOrder(String message) {
        try {
            RuskiSecurityHandler securityHandler = new RuskiSecurityHandler();
            String originalMessage = securityHandler.decryptMessage("supplying", "supplying", message);
            String destinationMessage = securityHandler.encryptMessage("supplying", "planner", originalMessage);
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            TextMessage txtMessage = session.createTextMessage(destinationMessage);
            MessageProducer producer = session.createProducer(queue);
            producer.send(txtMessage);
            session.close();
            connection.close();
        } catch (JMSException e) {
            log4jRuskiLogger.logError("Error enqueue order" + e.getMessage());
        } catch (HashGenerationException e) {
            log4jRuskiLogger.logError("Error to encode message order" + e.getMessage());
        } catch (HashSecurityError e) {
            log4jRuskiLogger.logError("Error to decode message order" + e.getMessage());
        }
    }
    
}
