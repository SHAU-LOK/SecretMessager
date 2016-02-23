package com.shootloking.secretmessager.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.event.NotifyReceiveEvent;
import com.shootloking.secretmessager.event.NotifySentSuccessEvent;
import com.shootloking.secretmessager.model.Contact;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.model.Message;
import com.shootloking.secretmessager.utility.RecycleViewSpacingDecoration;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.adapter.MessageListAdapter;
import com.shootloking.secretmessager.view.base.SMActivity;
import com.shootloking.secretmessager.view.dialog.ShowDefaultSmsDialog;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by shau-lok on 2/17/16.
 */
public class MessageListActivity extends SMActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycleView)
    RecyclerView recyclerView;
    @Bind(R.id.checkbox_encrypt)
    CheckBox checkbox_encrypt;
    @Bind(R.id.send)
    FloatingActionButton send;
    @Bind(R.id.composeEditText)
    EditText composeEditText;

    Conversation conv;
    Contact contact;
    LinearLayoutManager linearLayoutManager;
    long threadId = -1;

    MessageListAdapter adapter;


    @Override
    protected String getPageName() {
        return "MessageListActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_messagelist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFinish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        EventBus.getDefault().register(this);

        try {
            conv = (Conversation) getIntent().getSerializableExtra("conversation");
            threadId = conv.getThreadId();
            getSupportActionBar().setTitle(conv.getContact().getDisplayName());
        } catch (ClassCastException e) {
            Debug.error(getPageName(), e.toString());
            Toast.makeText(getSelfContext(), "解析数据库失败", Toast.LENGTH_SHORT).show();
        }
//        parseIntent(getIntent());
//        Uri uri = getIntent().getData();
//        try {
//            threadId = Integer.parseInt(uri.getLastPathSegment());
//            Debug.log(getPageName(), "threadId before: " + String.valueOf(threadId));
//        } catch (Exception e) {
//            Debug.error(getPageName(), "解析threadId失败\n" + e.toString());
//            Toast.makeText(getSelfContext(), "读取数据库失败", Toast.LENGTH_LONG).show();
//            mFinish();
//        }
        initAdapter();
    }


    private void parseIntent(Intent intent) {
        if (intent == null) return;

        Uri uri = intent.getData();

        if (uri != null) {
            if (!uri.toString().startsWith(Constants.MMS_SMS_CONVERSATION)) {
                uri = Uri.parse(Constants.MMS_SMS_CONVERSATION + uri.getLastPathSegment());
            }
        }

        try {
            threadId = Integer.parseInt(uri.getLastPathSegment());
            Debug.log(getPageName(), "threadId before: " + String.valueOf(threadId));
        } catch (Exception e) {
            Debug.error(getPageName(), "解析threadId失败\n" + e.toString());
            Toast.makeText(getSelfContext(), "读取数据库失败", Toast.LENGTH_LONG).show();
            mFinish();
        }

//        Observable<Conversation>.just(Conversation.getConversation(getSelfContext(), threadId)).

        Observable.just(Conversation.getConversation(getSelfContext(), threadId))
                .subscribe(new Action1<Conversation>() {
                    @Override
                    public void call(Conversation conversation) {
                        conv = conversation;
                    }
                });

//        Conversation conversations = Conversation.getConversation(getSelfContext(), threadId);
//        conv = conversations;
        threadId = conv.getThreadId();
        Debug.log(getPageName(), "threadId after: " + String.valueOf(threadId));

        Contact contact = conv.getContact();
        this.contact = contact;
        Debug.log(getPageName(), contact.toString());
        getSupportActionBar().setTitle(contact.getDisplayName());


        initAdapter();
    }

    private void initAdapter() {
        linearLayoutManager = new LinearLayoutManager(getSelfContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageListAdapter(getSelfContext());
        recyclerView.addItemDecoration(new RecycleViewSpacingDecoration(20));
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
//                super.onChanged();
//                linearLayoutManager
                int position = adapter.getItemCount() - 1;
                linearLayoutManager.smoothScrollToPosition(recyclerView, null, position);
            }
        });
        refresh();
    }


    public void refresh() {
        if (threadId <= 0) {
            return;
        }
        Observable<Cursor> myObservable = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor cursor = getSelfContext().getContentResolver().query(Uri.withAppendedPath(Uri.parse(Constants.SMS_CONVERSATION_URI), String.valueOf(threadId)), null, null, null, Message.SORT_ASC);
                Debug.log(getPageName(), DatabaseUtils.dumpCursorToString(cursor));
                if (Utils.isCursorValid(cursor)) {
                    subscriber.onNext(cursor);
//                    subscriber.onCompleted();
                }
            }
        });
        myObservable.subscribe(new Action1<Cursor>() {
            @Override
            public void call(Cursor cursor) {
                adapter.changeCursor(cursor);
            }
        });
    }


    private void send() {

        if (!TextUtils.isEmpty(composeEditText.getText())) {
            if (Build.VERSION.SDK_INT < 19) {
                sendSms();
            } else {
                if (!Utils.isDefaultApp(getSelfContext())) {
                    new ShowDefaultSmsDialog(getSelfContext()).show();
                } else {
                    sendSms();
                }
            }
        } else {
            Toast.makeText(getSelfContext(), "内容为空", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendSms() {
        if (checkbox_encrypt.isChecked()) {
            Toast.makeText(getSelfContext(), "加密中", Toast.LENGTH_SHORT).show();
            //// TODO: 2/19/16 加密处理
        }
        String body = composeEditText.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SEND);

        if (contact.getmNumber() == null) {
            intent.setData(Uri.parse("sms:"));
        } else {
            intent.setData(Uri.parse("smsto:" + contact.getmNumber()));
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra("sms_body", body);
        intent.putExtra("AUTOSEND", "1");

        startActivity(intent);
        composeEditText.setText("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void onEventMainThread(NotifySentSuccessEvent event) {
        if (event != null) {
            Debug.log(getPageName(), "更新数据");
            refresh();
        }

    }

    public void onEventMainThread(NotifyReceiveEvent event) {
        if (event != null) {
            Debug.log(getPageName(), "更新数据");
            refresh();
        }
    }


    public static void launch(Context context, Conversation conversation) {
//        Uri target = conversation.getUri();
        Intent intent = new Intent(context, MessageListActivity.class);
//        intent.setData(target);
        intent.putExtra("conversation", conversation);
        context.startActivity(intent);
    }


}
