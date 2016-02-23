package com.shootloking.secretmessager.model;

import android.net.Uri;
import android.text.TextUtils;

import com.shootloking.secretmessager.utility.Utils;

/**
 * Created by shau-lok on 2/17/16.
 */
public class Contact extends BaseModel {

    public static final String TAG = "contacts";


    private String mName="";
    private String mNumber="";
    private long mRecipientId = -1L;
    private String mLookupKey="";
    private long mPersonId = -1L;
    private Uri mContactUri;

    public Contact(long mRecipientId) {
        this.mRecipientId = mRecipientId;
    }

    public Contact(long mRecipientId, String mNumber) {
        this.mNumber = mNumber;
        this.mRecipientId = mRecipientId;
    }

    public String toString() {
        return "Contact Information: " +
                "\nDisplay Name: " + mName +
                "\nNumber: " + mNumber +
                "\nRecipitentId: " + mRecipientId +
                "\nLookupKey: " + mLookupKey +
                "\npersonId: " + mPersonId;
    }


    public String getDisplayName() {
        if (TextUtils.isEmpty(this.mName)) {
            if (TextUtils.isEmpty(this.mNumber)) {
                return "...";
            } else {
                return this.mNumber;
            }
        } else {
            return this.mName;
        }
    }


    public Uri getUri() {
        if (this.mContactUri == null && this.mPersonId > 0) {
            this.mContactUri = Utils.getContactUri(mPersonId);
        }
        return this.mContactUri;
    }


    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public long getmRecipientId() {
        return mRecipientId;
    }

    public void setmRecipientId(long mRecipientId) {
        this.mRecipientId = mRecipientId;
    }

    public String getmLookupKey() {
        return mLookupKey;
    }

    public void setmLookupKey(String mLookupKey) {
        this.mLookupKey = mLookupKey;
    }

    public long getmPersonId() {
        return mPersonId;
    }

    public void setmPersonId(long mPersonId) {
        this.mPersonId = mPersonId;
    }
}
