package com.shootloking.secretmessager.utility;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.text.SimpleDateFormat;

/**
 * Created by shau-lok on 1/30/16.
 */
public class Utils {


    /**
     * 返回 Contact Uri
     *
     * @param id
     * @return
     */
    public static Uri getContactUri(long id) {
        return ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
    }


    public static String DateFormat(Context context, long date) {
        return new SimpleDateFormat("yyyy-MM-dd h:mm a").format(date);
    }


    public static boolean isDefaultApp(final Context context) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) ||
                (context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context)));
    }

}
