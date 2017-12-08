/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.keygenerator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Rodrigo Stratta
 */
public class Starter {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            KeyGenerator keyGenerator = new KeyGenerator(1024);
            Scanner scanner = new Scanner(System.in);
            String keyName = "";
            while (!keyName.equalsIgnoreCase("exit")) {
                System.out.println("Input key name, or type exit to close app");
                keyName = scanner.next();
                if (StringUtils.isBlank(keyName)) {
                    System.out.println("Must input key name");
                } else {
                    try {
                        keyGenerator.createKeys(keyName);
                    } catch (IOException ex) {
                        System.out.println("Error creating key");
                    }
                }
            }
            
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (NoSuchProviderException ex) {
            System.out.println("Error creating key");
        }
        
    }
}
