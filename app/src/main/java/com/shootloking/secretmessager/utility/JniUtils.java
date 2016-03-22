package com.shootloking.secretmessager.utility;

/**
 * Created by shau-lok on 3/22/16.
 */
public class JniUtils {

    static {
        System.loadLibrary("sailear");
    }

    public native String getAESKey();


}
