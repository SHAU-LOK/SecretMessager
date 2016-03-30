package com.shootloking.secretmessager.data;

import android.net.Uri;

/**
 * 全 变量
 * Created by shau-lok on 1/19/16.
 */
public class Constants {

    public static boolean DEBUG = true;

    public static final String SMS_URI_STR = "content://sms";
    public static final String SMS_INBOX_URI_STR = SMS_URI_STR + "/inbox";
    public static final String SMS_SENT_URI_STR = SMS_URI_STR + "/sent";
    public static final String SMS_CONVERSATION_URI_STR = SMS_URI_STR + "/conversations";

    public static final String MMS_URI_STR = "content://mms-sms";
    public static final String MMS_INBOX_URI_STR = MMS_URI_STR + "/inbox";
    public static final String MMS_CONVERSATION_STR = MMS_URI_STR + "/conversations";
    public static final String MMS_CANONICAL_ADDRESS_STR = MMS_URI_STR + "/canonical-address";


    public static final Uri URI_SMS_INBOX = Uri.parse(SMS_INBOX_URI_STR);
    public static final Uri URI_SMS_SENT = Uri.parse(SMS_SENT_URI_STR);

    public static final Uri URI_SIMPLE = Uri.parse(MMS_CONVERSATION_STR).buildUpon().appendQueryParameter("simple", "true").build();
    public static final Uri URI_CONVERSATION = Uri.parse(MMS_CONVERSATION_STR);
    public static final Uri URI_CANONICAL_ADDRESS = Uri.parse(MMS_CANONICAL_ADDRESS_STR);


}
