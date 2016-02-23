package com.shootloking.secretmessager.task;

import android.content.Context;

import com.shootloking.secretmessager.data.ContactHelper;
import com.shootloking.secretmessager.model.Contact;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.adapter.ConversationListAdapter;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by shau-lok on 2/23/16.
 */
public final class ConversationListUpdateContactTask {

    public static ConversationListAdapter mAdapter = null;
    static final String TAG = "ConversationListUpdateContactTask";

    private Context context;
    private Conversation conversation;
    private long personId;

    public ConversationListUpdateContactTask(Context context, Conversation conversation, long personId) {
        this.context = context;
        this.conversation = conversation;
        this.personId = personId;
    }


    public void update() {

        if (context == null || conversation == null || personId < 0 || mAdapter == null) {
            Debug.error(TAG, "更新失败，不能为空");
            return;
        }
        Observable<Contact> observable = Observable.create(new Observable.OnSubscribe<Contact>() {
            @Override
            public void call(Subscriber<? super Contact> subscriber) {
                Contact newContact = ContactHelper.getContact(context, personId);
                subscriber.onNext(newContact);
        }
        });
        observable.subscribe(new Action1<Contact>() {
            @Override
            public void call(Contact contact) {
                conversation.setContact(contact);
//                mAdapter.notifyDataSetChanged();
            }
        });
    }


//    public static ConversationListAdapter getAdapter() {
//        return mAdapter;
//    }
//
//    public static void setAdapter(ConversationListAdapter adapter) {
//        mAdapter = adapter;
//    }
}
