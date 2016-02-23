package com.shootloking.secretmessager.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.shootloking.secretmessager.model.Contact;
import com.shootloking.secretmessager.utility.log.Debug;

/**
 * Created by shau-lok on 2/13/16.
 */
public class ContactHelper {

    public static final String TAG = "ContactHelper";

    public static String getName(Context context, String address) {

        if (address == null || address.isEmpty())
            return address;
        Cursor cursor;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
        ContentResolver contentResolver = context.getContentResolver();
        String name = address;

        try {
            cursor = contentResolver.query(uri, new String[]{BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            Debug.log(TAG, "Name Cursor: \n" + DatabaseUtils.dumpCursorToString(cursor));
            assert cursor != null;
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
            cursor.close();
        } catch (Exception e) {
            Debug.error(TAG, "Failed to find name for address " + address);
            e.printStackTrace();
        }
        return name;
    }


    public static Contact getContact(Context context, String address, long recipientId) {
        if (address == null || address.isEmpty())
            return new Contact(-1);
        Cursor cursor;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
        ContentResolver contentResolver = context.getContentResolver();
        Contact contact = new Contact(recipientId, address);
        try {
            cursor = contentResolver.query(uri, null, null, null, null);
//            Debug.log(TAG, "contact Cursor: \n" + DatabaseUtils.dumpCursorToString(cursor));

            assert cursor != null;
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                String lookup = cursor.getString(cursor.getColumnIndex("lookup"));
                long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
                contact.setmPersonId(id);
                contact.setmName(name);
                contact.setmLookupKey(lookup);
            }
            cursor.close();
        } catch (Exception e) {
            Debug.error(TAG, "Failed to find name for address " + address);
            e.printStackTrace();
        }
        return contact;

    }


    public static Contact getContact(Context context, long recipientId) {
        String number = null;
        try {
            Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(Constants.URI_CAONICAL_ADDRESS, recipientId), null, null, null, null);
//            Debug.log(TAG, DatabaseUtils.dumpCursorToString(cursor));
            assert cursor != null;
            if (cursor.moveToFirst()) {
                number = cursor.getString(cursor.getColumnIndex("address"));
            }
            cursor.close();
            return TextUtils.isEmpty(number) ? new Contact(recipientId) : getContact(context, number, recipientId);
        } catch (Exception e) {
            Debug.error(TAG, "fail to find name for recipientId " + recipientId);
        }
        return new Contact(recipientId);
    }


    public static String getName(Context context, long recipientId) {
        String number = null;
        try {
            Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(Constants.URI_CAONICAL_ADDRESS, recipientId), null, null, null, null);
            Debug.log(TAG, DatabaseUtils.dumpCursorToString(cursor));
            assert cursor != null;
            if (cursor.moveToFirst()) {
                number = cursor.getString(cursor.getColumnIndex("address"));
            }
            cursor.close();
            return TextUtils.isEmpty(number) ? "..." : getName(context, number);
        } catch (Exception e) {
            Debug.error(TAG, "fail to find name for recipientId " + recipientId);
        }
        return "...";
    }


}
