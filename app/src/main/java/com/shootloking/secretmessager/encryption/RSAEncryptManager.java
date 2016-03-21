package com.shootloking.secretmessager.encryption;

import android.content.Context;
import android.util.Base64;

import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by shau-lok on 3/21/16.
 */
public class RSAEncryptManager {


    private static RSAEncryptManager mInstance;

    public static final String TAG = "RSAEncryptManager";


    private static final String RSA_PRIVATE_KEY_FILE = "pkcs8_rsa_private_key.pem";
    private static final String RSA_PUBLIC_KEY_FILE = "rsa_public_key.pem";

    public static RSAEncryptManager getInstance() {
        if (mInstance == null) {
            return new RSAEncryptManager();
        } else {
            return mInstance;
        }
    }

    public RSAEncryptManager() {
    }


    public String encrypt(Context context, String plain) {
        return encrypt(context, RSA_PUBLIC_KEY_FILE, plain);
    }


    public String encrypt(Context context, String fileName, String plain) {

        InputStream inputstream = loadFromAsset(context, fileName);

        if (inputstream != null) {
            byte[] plains = plain.getBytes();
            PublicKey publicKey = RSAUtil.loadPublicKey(inputstream);
            long start = System.nanoTime();
            byte[] ciphers = RSAEncryption.encryptRSA(plains, publicKey);
            long end = System.nanoTime();
            long consume = end - start;
            String output = Base64.encodeToString(ciphers, Base64.NO_WRAP | Base64.URL_SAFE);


            StringBuilder builder = new StringBuilder();
            builder.append("加密字符串(RSA): \n");
            builder.append("公钥(PublicKey)信息: ").append(RSAUtil.logPublicKey(publicKey));
            builder.append("明文(String): ").append(plain).append("\n");
            builder.append("明文(hex): ").append(Utils.bytesToHex(plains)).append("\n");
            builder.append("明文(length): ").append(plains.length).append("字节\n");
            builder.append("密文(hex) :").append(Utils.bytesToHex(ciphers)).append("\n");
            builder.append("密文(length): ").append(ciphers.length).append("字节\n");
            builder.append("密文(base64): ").append(output).append("\n");
            builder.append("加密时长: ").append(consume).append("ns , 约: ").append(consume / 1000000.0f).append("ms").append("\n");

            Debug.log(TAG, builder.toString());

            return output;
        } else {
            com.shootloking.secretmessager.utility.log.Debug.error(TAG, "inputStream is null, load Asset fail");
            return null;
        }
    }


    public String decrypt(Context context, String cipher) {
        return decrypt(context, RSA_PRIVATE_KEY_FILE, cipher);
    }

    public String decrypt(Context context, String fileName, String cipher) {

        InputStream inputstream = loadFromAsset(context, fileName);

        if (inputstream != null) {
            byte[] ciphers = Base64.decode(cipher.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
            PrivateKey privateKey = RSAUtil.loadPrivateKey(inputstream);
            long start = System.nanoTime();
            byte[] plains = RSAEncryption.decryptRSA(ciphers, privateKey);
            long end = System.nanoTime();
            long consume = end - start;

            String plain = new String(plains);

            StringBuilder builder = new StringBuilder();
            builder.append("解密字符串(RSA): \n");
            builder.append("私钥(PrivateKey)信息: ").append(RSAUtil.logPrivateKey(privateKey));
            builder.append("密文(解base64前): ").append(cipher).append("\n");
            builder.append("密文(解base64前length): ").append(cipher.getBytes().length).append("字节\n");
            builder.append("密文(解base64后hex): ").append(Utils.bytesToHex(ciphers)).append("\n");
            builder.append("密文(解base64后length): ").append(ciphers.length).append("字节\n");
            builder.append("明文(String) :").append(plain).append("\n");
            builder.append("明文(hex): ").append(Utils.bytesToHex(plains)).append("\n");
            builder.append("明文(length): ").append(plains.length).append("字节\n");
            builder.append("解密时长: ").append(consume).append("ns , 约: ").append(consume / 1000000.0f).append("ms").append("\n");

            Debug.log(TAG, builder.toString());

            return plain;
        } else {
            com.shootloking.secretmessager.utility.log.Debug.error(TAG, "inputStream is null, load Asset fail");
            return null;
        }

    }


    private InputStream loadFromAsset(Context context, String fileName) {
        if (context == null) {
            com.shootloking.secretmessager.utility.log.Debug.error(TAG, "context should not be null");
            return null;
        }

        try {
            return context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
