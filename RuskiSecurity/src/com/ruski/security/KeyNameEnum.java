/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.security;

/**
 *
 * @author Rodrigo Stratta
 */
public enum KeyNameEnum {

    SUPPLYING("supplying"), SUPPLYING_FRONTEND("supplyingFrontend"), PLANNER("planner"), 
    PLANNER_FRONTEND("plannerFrontend"), PIPELINE("pipeline"), GOLIATH("goliath"), 
    GOLIATH_FRONTEND("goliathFrontend"), RUSKI("ruski");

    private String description;

    KeyNameEnum(String value) {
        description = value;
    }

    public String getDescription() {
        return description;
    }

    public static KeyNameEnum getByDescription(String description) {
        for (KeyNameEnum key : values()) {
            if (key.getDescription().equals(description)) {
                return key;
            }
        }
        return RUSKI;
    }
}
