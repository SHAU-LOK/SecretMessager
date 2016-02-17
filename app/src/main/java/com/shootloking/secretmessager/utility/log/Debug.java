package com.shootloking.secretmessager.utility.log;

import com.shootloking.secretmessager.data.Constants;

/**
 * Created by shau-lok on 1/19/16.
 */
public class Debug {

    public static void log(String tag, String msg) {
        if (Constants.DEBUG) {
            Logger.t(tag).d(msg);
        }
    }

    public static void verbose(String tag, String msg) {
        if (Constants.DEBUG) {
            Logger.t(tag).v(msg);
        }
    }

    public static void error(String tag, String msg) {
        if (Constants.DEBUG) {
            Logger.t(tag).e(msg);
        }
    }

    public static void json(String tag, String msg) {
        if (Constants.DEBUG) {
            Logger.json(msg);
        }
    }

    public static void write(String tag, String msg) {
        if (Constants.DEBUG) {
            Logger.t(tag).w(msg);
        }
    }
}
