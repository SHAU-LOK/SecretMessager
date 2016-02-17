package com.shootloking.secretmessager.utility;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.ContactsContract;

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


}
