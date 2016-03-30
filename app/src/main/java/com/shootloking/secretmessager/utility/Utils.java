package com.shootloking.secretmessager.utility;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.text.SimpleDateFormat;

/**
 * Created by shau-lok on 1/30/16.
 */
public class Utils {

    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

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
        return new SimpleDateFormat("M月dd日 ah:mm").format(date);
    }


    public static boolean isDefaultApp(final Context context) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) ||
                (context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context)));
    }

    public static boolean isCursorValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed();
    }


    /**
     * bytes 以16进制显示
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {

        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int tmp = bytes[i] & 0xff;
            hexChars[i * 2] = HEX_ARRAY[tmp >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[tmp & 0x0F];
        }
        return new String(hexChars);
    }


}
