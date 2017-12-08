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
public class KeyPathProvider {
    private static final String PRIVATE_KEY = "/privateKey";
    private static final String PUBLIC_KEY = "/publicKey";
    private static final String KEY_FOLDER = "C:/ruski/keyPair/";
    
    public static String getPublicPath(String apiName) {
        KeyNameEnum keyEnum = KeyNameEnum.getByDescription(apiName);
        switch (keyEnum) {
            case SUPPLYING:
                return KEY_FOLDER.concat(KeyNameEnum.SUPPLYING.getDescription()).concat(PUBLIC_KEY);
            case SUPPLYING_FRONTEND:
                return KEY_FOLDER.concat(KeyNameEnum.SUPPLYING_FRONTEND.getDescription()).concat(PUBLIC_KEY);
            case PLANNER:
                return KEY_FOLDER.concat(KeyNameEnum.PLANNER.getDescription()).concat(PUBLIC_KEY);
            case PLANNER_FRONTEND:
                return KEY_FOLDER.concat(KeyNameEnum.PLANNER_FRONTEND.getDescription()).concat(PUBLIC_KEY);
            case GOLIATH:
                return KEY_FOLDER.concat(KeyNameEnum.GOLIATH.getDescription()).concat(PUBLIC_KEY);
            case GOLIATH_FRONTEND:
                return KEY_FOLDER.concat(KeyNameEnum.GOLIATH_FRONTEND.getDescription()).concat(PUBLIC_KEY);
            case PIPELINE:
                return KEY_FOLDER.concat(KeyNameEnum.PIPELINE.getDescription()).concat(PUBLIC_KEY);
            default:
                return KEY_FOLDER.concat(KeyNameEnum.RUSKI.getDescription()).concat(PUBLIC_KEY);
        }
    }
    
    public static String getPrivatePath(String apiName) {
        KeyNameEnum keyEnum = KeyNameEnum.getByDescription(apiName);
        switch (keyEnum) {
            case SUPPLYING:
                return KEY_FOLDER.concat(KeyNameEnum.SUPPLYING.getDescription()).concat(PRIVATE_KEY);
            case SUPPLYING_FRONTEND:
                return KEY_FOLDER.concat(KeyNameEnum.SUPPLYING_FRONTEND.getDescription()).concat(PRIVATE_KEY);
            case PLANNER:
                return KEY_FOLDER.concat(KeyNameEnum.PLANNER.getDescription()).concat(PRIVATE_KEY);
            case PLANNER_FRONTEND:
                return KEY_FOLDER.concat(KeyNameEnum.PLANNER_FRONTEND.getDescription()).concat(PRIVATE_KEY);
            case GOLIATH:
                return KEY_FOLDER.concat(KeyNameEnum.GOLIATH.getDescription()).concat(PRIVATE_KEY);
            case GOLIATH_FRONTEND:
                return KEY_FOLDER.concat(KeyNameEnum.GOLIATH_FRONTEND.getDescription()).concat(PRIVATE_KEY);
            case PIPELINE:
                return KEY_FOLDER.concat(KeyNameEnum.PIPELINE.getDescription()).concat(PRIVATE_KEY);
            default:
                return KEY_FOLDER.concat(KeyNameEnum.RUSKI.getDescription()).concat(PRIVATE_KEY);
        }
    }
}
