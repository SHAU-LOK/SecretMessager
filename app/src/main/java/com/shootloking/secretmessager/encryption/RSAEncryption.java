package com.shootloking.secretmessager.encryption;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Created by shau-lok on 3/21/16.
 */
public class RSAEncryption {

    public static final String ALGORITHM = "RSA";

    public static byte[] encryptRSA(byte[] plain, PublicKey publicKey) {
        byte[] enc = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            enc = cipher.doFinal(plain);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return enc;

    }


    public static byte[] decryptRSA(byte[] enc, PrivateKey privateKey) {
        byte[] plain = null;

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            plain = cipher.doFinal(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plain;
    }


}
