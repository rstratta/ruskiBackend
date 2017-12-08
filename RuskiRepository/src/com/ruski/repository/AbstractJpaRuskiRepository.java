/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Rodrigo Stratta
 * @param <T>
 */
public abstract class AbstractJpaRuskiRepository<T extends AbstractEntity> {

    protected void addEntity(EntityManager entityManager, T entity) throws RepositoryException {
        try {
            entityManager.persist(entity);
        } catch (PersistenceException | IllegalStateException e) {
            throw new RepositoryException("Error agregando entidad", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }

        }
    }

    protected void updateEntity(EntityManager entityManager, T entity, Class entityClass) throws RepositoryException {
        try {
            T entityFromRepo = (T) entityManager.find(entityClass, entity.getId());
            if (entityFromRepo == null) {
                throw new RepositoryException("No se encontró la entidad a actualizar");
            }
            updateConcreteEntity(entityFromRepo, entity);
            entityManager.merge(entity);
        } catch (PersistenceException | IllegalStateException e) {
            throw new RepositoryException("Error actualizando entidad", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    protected List<T> getAll(EntityManager entityManager, String query) throws RepositoryException {
        try {
            Query queryResult = entityManager.createQuery(query);
            return (List<T>) queryResult.getResultList();
        } catch (PersistenceException | IllegalStateException e) {
            throw new RepositoryException("Error obteniendo entidad", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }

        }
    }
    
    protected T getById(EntityManager entityManager, Class entityClass, String id) throws RepositoryException {
        try {
            T entity = (T) entityManager.find(entityClass, id);
            if (entity == null) {
                throw new RepositoryException("No se encontro la entidad con id" + id);
            }
            return entity;
        } catch (PersistenceException | IllegalStateException e) {
            throw new RepositoryException("Error obteniendo entidad por id", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    protected void removeEntityById(EntityManager entityManager, 
            Class entityClass, String id) throws RepositoryException {
        try {
            T entity = (T) entityManager.find(entityClass, id);
            if (entity == null) {
                throw new RepositoryException("No existe la entidad a eliminar");
            }
            entityManager.remove(entity);
        } catch (PersistenceException | IllegalStateException e) {
            throw new RepositoryException("Error al eliminar entidad por id");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    protected abstract void updateConcreteEntity(T entityFromRepo, T entity);
}
