package com.shootloking.secretmessager.data;

import android.net.Uri;

/**
 * 全 变量
 * Created by shau-lok on 1/19/16.
 */
public class Constants {

    public static boolean DEBUG = true;

    public static final String SMS_URI = "content://mms-sms";
    public static final String SMS_INBOX_URI = SMS_URI + "/inbox";
    public static final String SMS_CONVERSATION_URI = SMS_URI + "/conversations";

    public static final Uri URI_SIMPLE = Uri.parse(SMS_CONVERSATION_URI).buildUpon().appendQueryParameter("simple", "true").build();
    public static final Uri CANONICAL_ADDRESS = Uri.parse(SMS_URI + "/canonical-address");
//    public static final Uri URI_SIMPLE = Uri.parse(SMS_CONVERSATION_URI + "?simple=true");
}
