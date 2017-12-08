/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.repository;

import com.ruski.goliath.actuator.entity.Actuator;
import com.ruski.logger.RuskiLogger;
import com.ruski.repository.AbstractJpaRuskiRepository;
import com.ruski.repository.RepositoryException;
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
public class JpaActuatorRepository extends AbstractJpaRuskiRepository<Actuator> implements ActuatorRepository {

    @PersistenceUnit
    protected EntityManagerFactory entityManagerfactory;
    @PersistenceContext
    private EntityManager entityManager;
    @EJB  private RuskiLogger log4jRuskiLogger;
    
    @Override
    public void addActuator(Actuator actuator) throws RepositoryException {
        log4jRuskiLogger.logInfo("Saving actuator in repository");
        entityManager = entityManagerfactory.createEntityManager();
        addEntity(entityManager, actuator);
        log4jRuskiLogger.logInfo("Finish saving actuator in repository");
    }

    @Override
    public List<Actuator> getAllActuators() throws RepositoryException {
        log4jRuskiLogger.logInfo("Getting all  actuator from repository");
        entityManager = entityManagerfactory.createEntityManager();
        List<Actuator> result = getAll(entityManager, "select a from goliath.actuators a");
        return result;
    }

    @Override
    public void updateActuator(Actuator actuator) throws RepositoryException {
        log4jRuskiLogger.logInfo("Saving actuator in repository");
        entityManager = entityManagerfactory.createEntityManager();
        updateEntity(entityManager, actuator, Actuator.class);
        log4jRuskiLogger.logInfo("Finish saving actuator in repository");
    }

    @Override
    public Actuator getActuatorById(String actuatorId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Getting actuator by id from repository");
        entityManager = entityManagerfactory.createEntityManager();
        return getById(entityManager, Actuator.class, actuatorId);
    }

    public void removeActuatorById(String actuatorId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Removing actuator in repository");
        removeEntityById(entityManager, Actuator.class, actuatorId);
    }
    
    @Override
    protected void updateConcreteEntity(Actuator entityFromRepo, Actuator entity) {
        entityFromRepo.setName(entity.getName());       
    }

    
}
