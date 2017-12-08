/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.goliath.actuator.repository;

import com.ruski.goliath.actuator.entity.Actuator;
import com.ruski.goliath.actuator.entity.ActuatorCommand;
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
import javax.persistence.TypedQuery;

/**
 *
 * @author Luis
 */
@Stateless
public class JpaCommandRepository extends AbstractJpaRuskiRepository<ActuatorCommand> 
        implements ActuatorCommandRepository {

    
    @PersistenceUnit
    protected EntityManagerFactory entityManagerfactory;
    @PersistenceContext
    private EntityManager entityManager;
    @EJB  private RuskiLogger log4jRuskiLogger;
    
    @Override
    public void addActuatorCommand(ActuatorCommand command) throws RepositoryException {
        log4jRuskiLogger.logInfo("Saving command in repository");
        entityManager = entityManagerfactory.createEntityManager();
        addEntity(entityManager, command);
        log4jRuskiLogger.logInfo("Finish saving command in repository");
    }
    
    @Override
    public List<ActuatorCommand> getCommandsFromActuator(Actuator actuator) throws RepositoryException{
        log4jRuskiLogger.logInfo("Getting command by actuator from  repository");
        entityManager = entityManagerfactory.createEntityManager();
        TypedQuery<ActuatorCommand> query = entityManager
                .createQuery("SELECT a FROM ActuatorCommand a WHERE a.actuator = :actuator", ActuatorCommand.class);
        return query.setParameter("actuator", actuator).getResultList();       
    }

    @Override
    public ActuatorCommand getActuatorCommandById(String id) throws RepositoryException{
        log4jRuskiLogger.logInfo("Getting ActuatorCommand by commandid from repository");
        entityManager = entityManagerfactory.createEntityManager();
        return getById(entityManager, ActuatorCommand.class, id);
    }

    @Override
    protected void updateConcreteEntity(ActuatorCommand entityFromRepo, ActuatorCommand entity) {
        entity.setActuator(entityFromRepo.getActuator());
        entity.setClassToExecute(entityFromRepo.getClassToExecute());
        entity.setClassToExecute(entity.getClassToExecute());
        entity.setMethodParam(entity.getMethodParam());
        entity.setCommand(entityFromRepo.getCommand());
        entity.setMethodToExecute(entityFromRepo.getMethodToExecute());        
    }
}
