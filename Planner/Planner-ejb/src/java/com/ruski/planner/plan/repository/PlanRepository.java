/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.planner.plan.repository;

import com.ruski.planner.plan.entity.Plan;
import com.ruski.repository.RepositoryException;
import java.util.List;

/**
 *
 * @author Rodrigo Stratta
 */
public interface PlanRepository {
    void addPlan(Plan plan) throws RepositoryException;

    void updatePlan(Plan plan) throws RepositoryException;

    Plan getPlanByOrderId(String orderId) throws RepositoryException;

    Plan getPlanById(String id) throws RepositoryException;

    List<Plan> getPendingsOfConfirmation() throws RepositoryException;
}
