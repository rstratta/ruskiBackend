/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.repository;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author Rodrigo Stratta
 */
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
