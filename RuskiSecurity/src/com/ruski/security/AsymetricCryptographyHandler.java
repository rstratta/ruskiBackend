/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.security;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author Rodrigo Stratta
 */
public class AsymetricCryptographyHandler {

    private Cipher cipher;

    public AsymetricCryptographyHandler() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("RSA");
    }

    public PrivateKey getPrivate(String appName) throws HashGenerationException {

        try {
            byte[] keyBytes = Files.readAllBytes(new File(KeyPathProvider.getPrivatePath(appName)).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException ex) {
            throw new HashGenerationException("Error getting key", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new HashGenerationException("Error getting key", ex);
        } catch (InvalidKeySpecException ex) {
            throw new HashGenerationException("Error getting key", ex);
        }

    }

    public PublicKey getPublic(String appName) throws HashGenerationException {
        try {
            byte[] keyBytes = Files.readAllBytes(new File(KeyPathProvider.getPublicPath(appName)).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException ex) {
            throw new HashGenerationException("Error getting key", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new HashGenerationException("Error getting key", ex);
        } catch (InvalidKeySpecException ex) {
            throw new HashGenerationException("Error getting key", ex);
        }
    }

    public String encryptText(Key key, String msg)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = blockCipher(msg.getBytes("UTF-8"), Cipher.ENCRYPT_MODE);
        char[] encryptedTranspherable = Hex.encodeHex(encrypted);
        return new String(encryptedTranspherable);
    }

    public String decryptText(Key key, String msg)
            throws InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, HashGenerationException {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bts = Hex.decodeHex(msg.toCharArray());
            byte[] decrypted = blockCipher(bts, Cipher.DECRYPT_MODE);
            return new String(decrypted, "UTF-8");
        } catch (DecoderException ex) {
            throw new HashGenerationException("Error to decode");
        }
    }

    private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
        byte[] scrambled;
        byte[] toReturn = new byte[0];
        int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;
        byte[] buffer = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            if ((i > 0) && (i % length == 0)) {
                scrambled = cipher.doFinal(buffer);
                toReturn = append(toReturn, scrambled);
                int newlength = length;
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                buffer = new byte[newlength];
            }
            buffer[i % length] = bytes[i];
        }
        scrambled = cipher.doFinal(buffer);
        toReturn = append(toReturn, scrambled);
        return toReturn;
    }

    private byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        for (int i = 0; i < prefix.length; i++) {
            toReturn[i] = prefix[i];
        }
        for (int i = 0; i < suffix.length; i++) {
            toReturn[i + prefix.length] = suffix[i];
        }
        return toReturn;
    }
}
