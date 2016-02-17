package com.shootloking.secretmessager.view.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.log.Debug;

/**
 * Created by shau-lok on 2/16/16.
 */
public final class ConversationAsyncHelper extends AsyncTask<Void, Void, Void> {

    static final String TAG = "ash";

    private static ConversationListAdapter adapter = null;

    private final Context context;

    private final Conversation conv;


    private ConversationAsyncHelper(final Context c, final Conversation con) {
        context = c;
        conv = con;
    }

    public static void fillConversation(final Context context, final Conversation c) {

        if (context == null || c == null || c.getThreadId() < 0) {
            return;
        }
        ConversationAsyncHelper helper = new ConversationAsyncHelper(context, c);
//        if (sync) {
//        helper.doInBackground((Void) null);
//        }
//        else {
//            try {
        helper.execute((Void) null);
//            } catch (RejectedExecutionException e) {
//                Log.e(TAG, "rejected execution", e);
//            }
//        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Void doInBackground(final Void... arg0) {
        if (conv == null) {
            return null;
        }
        Debug.log(TAG, "doInBackground()");
        try {
//            changed = conv.getContact().update(context, true,
//                    ConversationListActivity.showContactPhoto);

//            conv.setPersonName(ContactHelper.getContactName(context, conv.getPersonId()));
        } catch (NullPointerException e) {
            Log.e(TAG, "error updating contact", e);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(final Void result) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public static void setAdapter(final ConversationListAdapter a) {
        adapter = a;
    }
}

