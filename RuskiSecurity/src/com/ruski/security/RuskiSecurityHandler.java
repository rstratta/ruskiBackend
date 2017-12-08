/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.security;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;


/**
 *
 * @author Rodrigo Stratta
 */
public class RuskiSecurityHandler {

    private AsymetricCryptographyHandler asymetricCryptographyHandler;
    private Gson gson;
    
    

    public RuskiSecurityHandler() throws HashGenerationException {
        try {
            asymetricCryptographyHandler = new AsymetricCryptographyHandler();
            gson = new Gson();
        } catch (NoSuchAlgorithmException ex) {
            throw new HashGenerationException("Error encrypting hash", ex);
        } catch (NoSuchPaddingException ex) {
            throw new HashGenerationException("Error encrypting hash", ex);
        }
    }

    private String generateHashCode(String message) throws HashGenerationException {
        return  hashString(message);       
    }

    public String encryptMessage(String origApp, String destinationApp, 
            String requestBody) throws HashGenerationException {
        try {
            SecurityRequest request = new SecurityRequest();
            String base64Request = Base64.encodeBase64String(requestBody.getBytes());
            String hash = generateHashCode(base64Request);
            String encryptedHash = asymetricCryptographyHandler
                    .encryptText(asymetricCryptographyHandler.getPrivate(origApp), hash);
            request.setHashCode(encryptedHash);
            request.setRequestBody(base64Request);
            String jsonRequest = gson.toJson(request);
            String encryptedMessage = asymetricCryptographyHandler
                    .encryptText(asymetricCryptographyHandler.getPublic(destinationApp), jsonRequest);
            return encryptedMessage;
        } catch (NoSuchPaddingException | UnsupportedEncodingException 
                | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException ex) {
            throw new HashGenerationException("Error encrypting hash", ex);
        }
    }
    
    public String decryptMessage(String origApp, String destinationApp, String requestBody) 
            throws HashGenerationException, HashSecurityError {
        try {
            String jsonEncrypted = asymetricCryptographyHandler
                    .decryptText(asymetricCryptographyHandler.getPrivate(destinationApp), requestBody);
            SecurityRequest request = gson.fromJson(jsonEncrypted, SecurityRequest.class);
            String originalHash = asymetricCryptographyHandler
                    .decryptText(asymetricCryptographyHandler.getPublic(origApp), request.getHashCode());
            String requestHash = generateHashCode(request.getRequestBody());
            compareHash(originalHash, requestHash);
            return new String(Base64.decodeBase64(request.getRequestBody()));
        } catch (UnsupportedEncodingException | IllegalBlockSizeException 
                | BadPaddingException | InvalidKeyException ex) {
            throw new HashGenerationException("Error encrypting hash", ex);
        }
    }
    
    private void compareHash(String originalHash, String requestHash) throws HashSecurityError {
        byte[] origBytes = originalHash.getBytes();
        byte[] requestytes = requestHash.getBytes();
        for (int i = 0; i < requestytes.length; i++) {
            if ( origBytes[i] != requestytes[i]) {
                throw new HashSecurityError("Error on hash");
            }
        }
    }

    private static String hashString(String message)
            throws HashGenerationException {

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new HashGenerationException(
                    "Could not generate hash from input", ex);
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }

    
}
