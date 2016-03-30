package com.shootloking.secretmessager.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.shootloking.secretmessager.utility.log.Debug;

/**
 * Created by shau-lok on 2/13/16.
 */
public class InboxHelper {

    public static String TAG = "Inbox Helper";

    public static void RefreshInbox(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor smsCursor = contentResolver.query(Uri.parse(Constants.SMS_URI_STR), null, null, null, null);
        int totalCount = smsCursor.getCount();
        while (smsCursor.moveToFirst()) {
            Debug.log("sms", "contactNumber: " + smsCursor.getColumnIndexOrThrow("address") + "msg: " + smsCursor.getColumnIndexOrThrow("body") + " _id: " + smsCursor.getColumnIndexOrThrow("_id") + " Person: ");
            smsCursor.moveToNext();
        }
        smsCursor.close();
    }


}
