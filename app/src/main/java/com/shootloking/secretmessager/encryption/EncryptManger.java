package com.shootloking.secretmessager.encryption;

import android.util.Base64;

import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
            //生成16byte
            byte[] value = digest.digest();
//            return digest.digest();
//            Debug.log(TAG, "md5后的长度: " + value.length + " md5.toString(): " + value.toString());
            return value;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }


    private byte[] randomGen16Byte() {

        byte[] iv = new byte[16];
        SecureRandom ran = new SecureRandom();
        ran.nextBytes(iv);

        long len = iv.length;
//        Debug.log(TAG, "random生成的iv长度: " + iv.length + " iv.toString(): " + iv.toString());

        return iv;
    }


    public String Encrypt(String plain) throws Exception {
//        byte[] iv = gen16ByteFromStr(IV_KEY);
        byte[] iv2 = randomGen16Byte(); //iv随机生成
//        return Encrypt(plain, iv);
        return Encrypt(plain, iv2);
    }

    public String Encrypt(String plain, byte[] iv) throws Exception {
        if (encryption == null) {
            initKey();
        }
        byte[] plains = plain.getBytes();
        int len = plains.length;
        byte[] cipher = new byte[len];
        int iv_len = iv.length;
        encryption.CFBEncrypt(plains, cipher, iv, len);
        //iv加到密文最前
        byte[] cipherAndIv = new byte[iv_len + cipher.length];
        for (int i = 0; i < cipherAndIv.length; i++) {
            cipherAndIv[i] = i < iv_len ? iv[i] : cipher[i - iv_len];
        }

        String output = Base64.encodeToString(cipherAndIv, Base64.NO_WRAP | Base64.URL_SAFE);

        Debug.log(TAG, "加密字符串(AES128_CFB): \n" +
                "plain text: " + plain + "\nplain.getByte(): " + Utils.bytesToHex(plains) + "\nplain.length(): " + len + "字节\n" +
                "iv.getByte(): " + Utils.bytesToHex(iv) + "\niv.length(): " + iv.length + "字节\n" +
                "cipher.getByte(): " + Utils.bytesToHex(cipher) + "\ncipher.length(): " + cipher.length + "字节\n" +
                "cipherAndIv.getByte(): " + Utils.bytesToHex(cipherAndIv) + "\ncipherAndIv.length(): " + cipherAndIv.length + "字节\n" +
                "加密Base64前: " + Utils.bytesToHex(cipherAndIv) + "\n加密Base64后: " + output
        );

        return output;
    }

//    public String Decrypt(String cipher) throws Exception {
//        // TODO: 3/20/16 iv需要截取cipher.getByte[]前16byte
//        byte[] iv = gen16ByteFromStr(IV_KEY);
//        return Decrypt(cipher, iv);
//    }

    public String Decrypt(String cipher) throws Exception {
        if (encryption == null) {
            initKey();
        }

        byte[] ivs = new byte[16];
        byte[] ciphers = Base64.decode(cipher, Base64.NO_WRAP | Base64.URL_SAFE);
        byte[] ciphersKillIv = new byte[ciphers.length - ivs.length];


        for (int i = 0; i < ciphers.length; i++) {
//            ivs[i] = ciphers[i];
            if (i < ivs.length) {
                ivs[i] = ciphers[i];
            } else {
                ciphersKillIv[i - ivs.length] = ciphers[i];
            }
        }


        int len = ciphersKillIv.length;
        byte[] plains = new byte[len];
//        encryption.CFBDecrypt(ciphers, plains, iv, len);
        encryption.CFBDecrypt(ciphersKillIv, plains, ivs, len);

        String output = new String(plains);

        Debug.log(TAG, "解密字符串(AES128_CFB): \n" +
                "解密Base64前: " + cipher + " \n解密Base64后: " + Utils.bytesToHex(ciphers) + "\n" +
                "CipherAndIv.getByte(): " + Utils.bytesToHex(ciphers) + "\nCipherAndIv.length(): " + ciphers.length + "字节\n" +
                "iv.getByte(): " + Utils.bytesToHex(ivs) + "\niv.length(): " + ivs.length + "\n" +
                "cipher.getByte(): " + Utils.bytesToHex(ciphersKillIv) + "\ncipher.length(): " + ciphersKillIv.length + "字节\n" +
                "plain text: " + output + "\nplain.getByte(): " + Utils.bytesToHex(plains) + "\nplain.length(): " + plains.length + "字节\n"
        );

        return output;
    }


}
