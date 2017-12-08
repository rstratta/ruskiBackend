/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.supplying.order.repository;

import com.ruski.logger.RuskiLogger;
import com.ruski.repository.AbstractJpaRuskiRepository;
import com.ruski.repository.RepositoryException;
import com.ruski.supplying.order.entity.Order;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;


/**
 *
 * @author Luis
 */
@Stateless
public class JpaOrderRepository extends AbstractJpaRuskiRepository<Order> implements OrderRepository {

    @PersistenceUnit
    protected EntityManagerFactory entityManagerfactory;
    @PersistenceContext
    private EntityManager entityManager;
    @EJB  private RuskiLogger log4jRuskiLogger;
    
    @Override
    public void addOrder(Order order) throws RepositoryException {
        log4jRuskiLogger.logInfo("Starting saving order on repository");
        entityManager = entityManagerfactory.createEntityManager();
        addEntity(entityManager, order);
        log4jRuskiLogger.logInfo("Finish saving order on repository");
    }

    @Override
    public List<Order> getAllOrders() throws RepositoryException {
        log4jRuskiLogger.logInfo("Getting all orders from repository");
        entityManager = entityManagerfactory.createEntityManager();
        List<Order> result = getAll(entityManager, "select o from Order o");
        log4jRuskiLogger.logInfo("Finish getting all orders from repository");
        return result;
    }

    @Override
    public void updateOrder(Order order) throws RepositoryException {
        log4jRuskiLogger.logInfo("Starting saving order on repository " + order );
        entityManager = entityManagerfactory.createEntityManager();
        updateEntity(entityManager, order, Order.class);
        log4jRuskiLogger.logInfo("Starting saving order on repository");
    }

    @Override
    public Order getOrderById(String orderId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Starting getting order by id from repository " + orderId);
        entityManager = entityManagerfactory.createEntityManager();
        Order result = getById(entityManager, Order.class, orderId);
        log4jRuskiLogger.logInfo("Finish getting order by id from  repository");
        return result;
    }

    public void removeOrderById(String orderId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Starting removing order on repository " + orderId);
        removeEntityById(entityManager, Order.class, orderId);
        log4jRuskiLogger.logInfo("Finish removing order on repository");
    }
    
    @Override
    protected void updateConcreteEntity(Order entityFromRepo, Order entity) {
        entityFromRepo.setBillingCloseday(entity.getBillingCloseday());
        entityFromRepo.setClientId(entity.getClientId());
        entityFromRepo.setDateFrom(entity.getDateFrom());
        entityFromRepo.setServicePointId(entity.getServicePointId());
        entityFromRepo.setVolume(entity.getVolume());
        entityFromRepo.setDeleted(entity.isDeleted());
    }

    
}
