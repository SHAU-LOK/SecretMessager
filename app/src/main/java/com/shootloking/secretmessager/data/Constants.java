package com.shootloking.secretmessager.data;

import android.net.Uri;

/**
 * 全 变量
 * Created by shau-lok on 1/19/16.
 */
public class Constants {

    public static boolean DEBUG = true;

    public static final String SMS_URI = "content://sms";
    public static final String SMS_INBOX_URI = SMS_URI + "/inbox";
    public static final String SMS_SENT_URI = SMS_URI + "/sent";


    public static final Uri URI_SMS_INBOX = Uri.parse(SMS_INBOX_URI);
    public static final Uri URI_SMS_SENT = Uri.parse(SMS_SENT_URI);


    public static final String MMS_SMS_URI = "content://mms-sms";
    public static final String MMS_SMS_INBOX_URI = MMS_SMS_URI + "/inbox";
    public static final String MMS_SMS_CONVERSATION = MMS_SMS_URI + "/conversations";

    public static final Uri URI_SIMPLE = Uri.parse(MMS_SMS_CONVERSATION).buildUpon().appendQueryParameter("simple", "true").build();
    public static final Uri URI_CONVERSATION = Uri.parse(MMS_SMS_CONVERSATION);
    public static final Uri URI_CAONICAL_ADDRESS = Uri.parse(MMS_SMS_URI + "/canonical-address");
//    public static final Uri URI_SIMPLE = Uri.parse(MMS_SMS_CONVERSATION + "?simple=true");


}
