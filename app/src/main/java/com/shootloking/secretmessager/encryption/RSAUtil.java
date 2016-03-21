package com.shootloking.secretmessager.encryption;

import android.util.Base64;

import com.shootloking.secretmessager.utility.log.Debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by shau-lok on 3/21/16.
 */
public class RSAUtil {

    public static final String TAG = "RSAUtil";
    public static final String ALGORITHM = "RSA";
    public static final int KEY_LENGTH = 2048;


    /**
     * 自动生成KeyPair
     *
     * @return
     */
    public static KeyPair generateRSAKey() {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(ALGORITHM);
            kpg.initialize(KEY_LENGTH);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPublicExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPublicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    /**
     * openssl 生成的rsa_key.pem 经过base64.
     * 公钥存储在
     * "main/assets/rsa_public_key.pem"
     *
     * @param publicKeyStr
     * @return
     */
    public static PublicKey loadPublicKey(String publicKeyStr) {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
            KeyFactory keyFactory;
            keyFactory = KeyFactory.getInstance(ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * openssl 生成的rsa_key.pem 经过base64.
     * 私钥存储在
     * "main/assets/pkcs8_rsa_private_key.pem"
     *
     * @param privateKeyStr
     * @return
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);
            KeyFactory keyFactory;
            keyFactory = KeyFactory.getInstance(ALGORITHM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static PublicKey loadPublicKey(InputStream inputStream) {
        try {
            return loadPublicKey(readKey(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PrivateKey loadPrivateKey(InputStream inputStream) {
        try {
            return loadPrivateKey(readKey(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readKey(InputStream inputStream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String readLine;
        StringBuilder builder = new StringBuilder();
        while ((readLine = reader.readLine()) != null) {
            if (readLine.charAt(0) != '-') {
                builder.append(readLine);
                builder.append("\r");
            }
        }
        return builder.toString();
    }

    public static String logPublicKey(PublicKey publicKey) {
        StringBuilder builder = new StringBuilder();
        RSAPublicKey key = (RSAPublicKey) publicKey;
        builder.append("\n  RSA公钥Key: \n");
        builder.append("    Modulus.length= ").append(key.getModulus().bitLength()).append("\n");
        builder.append("    Modulus= ").append(key.getModulus().toString()).append("\n");
        builder.append("    PublicExponent.length= ").append(key.getPublicExponent().bitLength()).append("\n");
        builder.append("    PublicExponent= ").append(key.getPublicExponent().toString()).append("\n\n");
        return builder.toString();
    }

    public static String logPrivateKey(PrivateKey privateKey) {
        StringBuilder builder = new StringBuilder();
        RSAPrivateKey key = (RSAPrivateKey) privateKey;
        builder.append("\n  RSA私钥Key: \n");
        builder.append("    Modulus.length= ").append(key.getModulus().bitLength()).append("\n");
        builder.append("    Modulus= ").append(key.getModulus().toString()).append("\n");
        builder.append("    PrivateExponent.length= ").append(key.getPrivateExponent().bitLength()).append("\n");
        builder.append("    PrivateExponent= ").append(key.getPrivateExponent().toString()).append("\n\n");
        Debug.log(TAG, builder.toString());
        return builder.toString();
    }


}
