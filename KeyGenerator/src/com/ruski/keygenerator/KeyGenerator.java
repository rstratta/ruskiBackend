/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ruski.keygenerator;


import com.ruski.security.KeyPathProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author Rodrigo Stratta
 */
public class KeyGenerator {
    
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeyGenerator(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keylength);
    }

    public void createKeys(String path) throws IOException {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
        writeToFile(KeyPathProvider.getPrivatePath(path), privateKey.getEncoded());
        writeToFile(KeyPathProvider.getPublicPath(path), publicKey.getEncoded());
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private void writeToFile(String path, byte[] key) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            fos.write(key);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error al generar clave");
        } catch (IOException ex) {
            System.out.println("Error al generar clave");
        } finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException ex) {
                System.out.println("Error al generar clave");
            }
        }
    }

}
