package com.shootloking.secretmessager.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import com.shootloking.secretmessager.data.ContactHelper;

/**
 * Created by shau-lok on 2/18/16.
 */
public class Message extends BaseModel {

    public static final String TAG = "message";

    public static final String SORT_ASC = CallLog.Calls.DATE + " ASC";

    public static final String SORT_DESC = CallLog.Calls.DATE + " DESC";

    private int msg_id;
    private int thread_id;
    private String address;
    private String body;
    private String timeStamp;
    private long date;
    private Uri msg_uri;
    private int msg_type; // 1: in 2: out
    private int read;

    private Contact contact;
    Context context;

    static Uri mUri;


    public Message(Context context, Cursor cursor) {
        this.context = context;
        this.msg_id = cursor.getInt(cursor.getColumnIndex("_id"));
        this.thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
        this.address = cursor.getString(cursor.getColumnIndex("address"));
        this.body = cursor.getString(cursor.getColumnIndex("body"));
        this.date = cursor.getLong(cursor.getColumnIndex("date"));
        this.msg_type = cursor.getInt(cursor.getColumnIndex("type"));
        this.read = cursor.getInt(cursor.getColumnIndex("read"));
        this.contact = ContactHelper.getContact(context, thread_id);
    }

    public static Message getMessage(Context context, Cursor cursor) {
        return new Message(context, cursor);
    }

    public static Message getMessage(Context context, Uri mUrii) {
        mUri = mUrii;
        Cursor cursor = context.getContentResolver().query(mUrii, null, null, null, null);
//        Debug.log(TAG, DatabaseUtils.dumpCursorToString(cursor));
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return getMessage(context, cursor);
    }

//    public static ArrayList<Message> getMessageArrayList(Context context, int threadId, Uri uri) {
//
//        ArrayList<Message> messages = new ArrayList<>();
////        Cursor cursor = context.getContentResolver().query(Uri.parse(Constants.MMS_URI_STR), null, "where thread_id=" + threadId , null, null);
//        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(Uri.parse("content://sms/conversations"), String.valueOf(threadId)), null, null, null, SORT_ASC);
////        Debug.log(TAG , "withAppendedPath:  " + Uri.withAppendedPath(Constants.URI_CONVERSATION, String.valueOf(threadId)).toString());
//        Debug.log(TAG, DatabaseUtils.dumpCursorToString(cursor));
//        assert cursor != null;
//
//        if (cursor.moveToFirst()) {
//            do {
//                Message message = null;
//                message = getMessage(context, cursor);
//                messages.add(message);
//            } while (cursor.moveToNext());
//        } else {
//            Debug.error(TAG, "did not found message: " + String.valueOf(threadId));
//        }
//        cursor.close();
//        return messages;
//    }


    //GETTER & SETTER

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Uri getMsg_uri() {
        return msg_uri;
    }

    public void setMsg_uri(Uri msg_uri) {
        this.msg_uri = msg_uri;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
