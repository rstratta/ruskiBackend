/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.repository;


import com.ruski.planner.plan.entity.Plan;
import com.ruski.logger.RuskiLogger;
import com.ruski.repository.AbstractJpaRuskiRepository;
import com.ruski.repository.RepositoryException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;


/**
 *
 * @author Rodrigo Stratta
 */
@Stateless
public class JpaPlanRepository  extends AbstractJpaRuskiRepository<Plan> implements PlanRepository {

    @PersistenceUnit
    protected EntityManagerFactory entityManagerfactory;
    @PersistenceContext
    private EntityManager entityManager;
    @EJB  private RuskiLogger log4jRuskiLogger;
    
    @Override
    protected void updateConcreteEntity(Plan entityFromRepo, Plan entity) {
    }

    @Override
    public void addPlan(Plan plan) throws RepositoryException{
        log4jRuskiLogger.logInfo("Add plan on repository " + plan);
        entityManager = entityManagerfactory.createEntityManager();
        addEntity(entityManager, plan);
    }
    
    @Override
     public void updatePlan(Plan plan) throws RepositoryException {
        log4jRuskiLogger.logInfo("Update plan on repository " + plan);
        entityManager = entityManagerfactory.createEntityManager();
        updateEntity(entityManager, plan, Plan.class);
    }

    @Override
    public Plan getPlanByOrderId(String orderId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Get plan by OrderId " + orderId);
        entityManager = entityManagerfactory.createEntityManager();
        try {
            String planQuery = "select p from Plan p where p.orderId=:orderId";
            Query queryResult = entityManager.createQuery(planQuery);
            queryResult.setParameter("orderId", orderId);
            List<Plan> result = queryResult.getResultList();
            if (result.isEmpty()) {
                throw new RepositoryException("Plan with orderId: " + orderId + " not found");
            }
            log4jRuskiLogger.logInfo("Finish get plan by orderId");
            return result.get(0);
        } catch (PersistenceException | IllegalStateException e) {
            log4jRuskiLogger.logError("Error on persist entity" + e.getMessage());
            throw new RepositoryException("Error getting plan by orderId");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }

        }
    }

    @Override
    public Plan getPlanById(String id) throws RepositoryException {
        log4jRuskiLogger.logInfo("Get plan by id from repository" + id);
        entityManager = entityManagerfactory.createEntityManager();
        return getById(entityManager, Plan.class, id);
    }

    @Override
    public List<Plan> getPendingsOfConfirmation() throws RepositoryException {
        log4jRuskiLogger.logInfo("Get pending plan of confirmatio ");
        entityManager = entityManagerfactory.createEntityManager();
        try {
            String planQuery = "select p from Plan p where p.planState=:state";
            Query queryResult = entityManager.createQuery(planQuery);
            queryResult.setParameter("state", Plan.PENDING_OF_APPROVATION);
            List<Plan> result = queryResult.getResultList();
            return result;
        } catch (PersistenceException | IllegalStateException e) {
            log4jRuskiLogger.logError("Error on persist entity" + e.getMessage());
            throw new RepositoryException("Error getting plan pending confirm");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }

        }
    }
}
