/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.section.repository;

import com.ruski.planner.section.entity.Section;
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
public class JpaSectionRepository extends AbstractJpaRuskiRepository<Section> implements SectionRepository {
    @PersistenceUnit
    protected EntityManagerFactory entityManagerfactory;
    @PersistenceContext
    private EntityManager entityManager;
    @EJB  private RuskiLogger log4jRuskiLogger;

    @Override
    protected void updateConcreteEntity(Section entityFromRepo, Section entity) {
        entityFromRepo.setActuatorId(entity.getActuatorId());
        entityFromRepo.setSourceId(entity.getSourceId());
    }

    @Override
    public void addSection(Section section) throws RepositoryException {
        log4jRuskiLogger.logInfo("Save secrion in repository " + section);
        entityManager = entityManagerfactory.createEntityManager();
        addEntity(entityManager, section);
    }

    @Override
    public void removeSection(String sectionId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Remove secrion in repository, sectionId " + sectionId);
        entityManager = entityManagerfactory.createEntityManager();
        removeEntityById(entityManager, Section.class, sectionId);
    }

    @Override
    public List<Section> getSectionsByPlanId(String planId) throws RepositoryException {
        log4jRuskiLogger.logInfo("Get secrion by planId " + planId);
        entityManager = entityManagerfactory.createEntityManager();
        try {
            String sectionQuery = "select s from Section s where s.plan.id=:planId";
            Query queryResult = entityManager.createQuery(sectionQuery);
            queryResult.setParameter("planId", planId);
            List<Section> result = queryResult.getResultList();
            if (result.isEmpty()) {
                throw new RepositoryException("Section with planId: " + planId + " not found");
            }
            return result;
        } catch (PersistenceException | IllegalStateException e) {
            log4jRuskiLogger.logError("Error on persist entity" + e.getMessage());
            throw new RepositoryException("Error getting section by planId");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }

        }
    }
}
