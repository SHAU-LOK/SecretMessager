package com.shootloking.secretmessager.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony;

import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.data.ContactHelper;
import com.shootloking.secretmessager.utility.log.Debug;

/**
 * Created by shau-lok on 2/13/16.
 */
public class Conversation extends BaseModel {

    public static final String TAG = "ConversionModel";

    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_COUNT = "message_count";
    public static final String COLUMN_PERSON = "recipient_ids";
    public static final String COLUMN_BODY = "snippet";
    public static final String COLUMN_READ = "read";

    public static final String SORT_ORDER = Telephony.Sms.DEFAULT_SORT_ORDER;


    public static final int INDEX_ID = 0;
    public static final int INDEX_DATE = 1;
    public static final int INDEX_COUNT = 2;
    public static final int INDEX_PERSON = 3;
    public static final int INDEX_BODY = 4;
    public static final int INDEX_READ = 5;


    public static final String[] PROJECTION = new String[]{
            COLUMN_ID,
            COLUMN_DATE,
            COLUMN_COUNT,
            COLUMN_PERSON,
            COLUMN_BODY,
            COLUMN_READ
    };

    private int id;
    private int threadId;
    private long personId;
    private long date;

    private String body;
    private int read;
    private Contact contact;
    private int count = -1;

    private String personName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Conversation(Context context, Cursor cursor) {
        this.threadId = cursor.getInt(INDEX_ID);
        this.date = cursor.getLong(INDEX_DATE);
        this.body = cursor.getString(INDEX_BODY);
        this.read = cursor.getInt(INDEX_READ);
        this.count = cursor.getInt(INDEX_COUNT);
        this.personId = cursor.getLong(INDEX_PERSON);//联系人id
//        this.personName = ContactHelper.getName(context, personId);
        this.contact = ContactHelper.getContact(context, personId);
//        this.contact = new Contact(personId);
//        ConversationListUpdateContactTask task = new ConversationListUpdateContactTask(context,this, personId);
//        task.update();
    }


    public static Conversation getConversation(Context context, Cursor cursor) {
        return new Conversation(context, cursor);
    }

    public static Conversation getConversation(Context context, long threadId) {
        Conversation conversation = null;
        Cursor cursor = context.getContentResolver().query(Constants.URI_SIMPLE, PROJECTION,
                COLUMN_ID + " = ?", new String[]{String.valueOf(threadId)}, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            conversation = getConversation(context, cursor);
        } else {
            Debug.error(TAG, "did not found conversation: " + String.valueOf(threadId));
        }
        cursor.close();
        return conversation;
    }


    //GETTER & SETTER
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Uri getUri() {
        return Uri.withAppendedPath(Uri.parse(Constants.MMS_SMS_CONVERSATION), String.valueOf(threadId));
    }
}
