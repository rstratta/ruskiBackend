/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.keygenerator;

import com.ruski.security.HashGenerationException;
import com.ruski.security.HashSecurityError;
import com.ruski.security.RuskiSecurityHandler;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Rodrigo Stratta
 */
public class RequestEncoder {

    public static void main(String[] args) {
        try {
            RuskiSecurityHandler securityHandler = new RuskiSecurityHandler();
            Scanner scanner = new Scanner(System.in);
            String origAppName = "";
            String destinationAppName = "";
            String requestBody = "";
            while (!origAppName.equalsIgnoreCase("exit")) {
                System.out.println("Input orig app name, or type exit to close app");
                origAppName = scanner.nextLine();
                System.out.println("Input destination app name, or type exit to close app");
                destinationAppName = scanner.nextLine();
                System.out.println("Input request body");
                requestBody = scanner.nextLine();
                if (!origAppName.equalsIgnoreCase("exit") && !destinationAppName.equalsIgnoreCase("exit")
                        && !requestBody.equalsIgnoreCase("exit")) {
                    if (StringUtils.isBlank(origAppName)) {
                        System.out.println("Must input key name");
                    } else if (StringUtils.isBlank(destinationAppName)) {
                        System.out.println("Must input key name");
                    } else if (StringUtils.isBlank(requestBody)) {
                        System.out.println("Must input a request body");
                    } else {
                        String result = securityHandler.encryptMessage(origAppName, destinationAppName, requestBody);
                        System.out.println(result);
                        String decryptBody = securityHandler.decryptMessage(origAppName, destinationAppName, result);
                        System.out.println(decryptBody);

                    }
                }
                origAppName = "";
                destinationAppName = "";
                requestBody = "";
            }

        } catch (HashGenerationException ex) {
            System.out.println("Error encripting request");
        } catch (HashSecurityError e) {
            System.out.println("Error with hash");
        }

    }
}
