package com.shootloking.secretmessager.encryption;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shau-lok on 3/6/16.
 */
public class EncryptManger {

    public static final String TAG = "EncryptManger";

    private static EncryptManger mInstance;

    private AESEncryption encryption;

    private static final String KEY = "AES-128 SecretMessager";

    private static final String IV_KEY = "HelloWorld";

    public static EncryptManger getInstance() {
        if (mInstance == null) {
            return new EncryptManger();
        } else {
            return mInstance;
        }
    }

    public EncryptManger() {
        initKey();
    }

    public void initKey() {
        encryption = new AESEncryption();
        encryption.KeyExpansion(gen16ByteFromStr(KEY));
    }


    private byte[] gen16ByteFromStr(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            return digest.digest();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }


    public String Encrypt(String plain) throws Exception {
        byte[] iv = gen16ByteFromStr(IV_KEY);
        return Encrypt(plain, iv);
    }

    public String Encrypt(String plain, byte[] iv) throws Exception {
        if (encryption == null) {
            initKey();
        }
        byte[] plains = plain.getBytes();
        int len = plains.length;
        byte[] cipher = new byte[len];

        encryption.CFBEncrypt(plains, cipher, iv, len);
        return Base64.encodeToString(cipher, Base64.NO_WRAP | Base64.URL_SAFE);
    }

    public String Decrypt(String cipher) throws Exception {
        byte[] iv = gen16ByteFromStr(IV_KEY);
        return Decrypt(cipher, iv);
    }

    public String Decrypt(String cipher, byte[] iv) throws Exception {
        if (encryption == null) {
            initKey();
        }
        byte[] ciphers = Base64.decode(cipher, Base64.NO_WRAP | Base64.URL_SAFE);
        int len = ciphers.length;
        byte[] plains = new byte[len];
        encryption.CFBDecrypt(ciphers, plains, iv, len);
        return new String(plains);
    }


}
