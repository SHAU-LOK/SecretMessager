package com.shootloking.secretmessager.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.shootloking.secretmessager.event.EncryptEvent;
import com.shootloking.secretmessager.event.NotifyReceiveEvent;
import com.shootloking.secretmessager.event.NotifySentSuccessEvent;
import com.shootloking.secretmessager.model.Contact;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.model.Message;
import com.shootloking.secretmessager.sms.Transactions;
import com.shootloking.secretmessager.task.EncryptSendAsyncTask;
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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    Subscription mSubscription;


    ProgressDialog progressDialog;

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
            contact = conv.getContact();
            getSupportActionBar().setTitle(conv.getContact().getDisplayName());
        } catch (ClassCastException e) {
            Debug.error(getPageName(), e.toString());
            Toast.makeText(getSelfContext(), "解析数据库失败", Toast.LENGTH_SHORT).show();
        }
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
//                Debug.log(getPageName(), DatabaseUtils.dumpCursorToString(cursor));
                if (Utils.isCursorValid(cursor)) {
                    subscriber.onNext(cursor);
//                    subscriber.onCompleted();
                }
            }
        });
        mSubscription = myObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
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
        String body = composeEditText.getText().toString().trim();
        if (checkbox_encrypt.isChecked()) {
//            Toast.makeText(getSelfContext(), "加密中", Toast.LENGTH_SHORT).show();
            //加密处理线程处理
//            try {
//                body = EncryptManger.getInstance().Encrypt(body);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(getSelfContext(), "加密失败", Toast.LENGTH_SHORT).show();
//            }
            EncryptSendAsyncTask task = new EncryptSendAsyncTask(this);
            task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, body);
        } else {

            //不加密
            Description = "不加密处理";
            EncryptConsume = 0;
            beforeLength = String.valueOf(body.getBytes().length);
            afterLength = beforeLength;
            plainText = body;
            cipherText = body;
//            beforeSendTime = System.currentTimeMillis();
            beforeSendTime = System.nanoTime();
            Transactions transactions = new Transactions(this);
            transactions.sendMessage(body, contact.getmNumber());
//            composeEditText.setText("");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * 加密完成
     *
     * @param event
     */
    public void onEventMainThread(EncryptEvent event) {
        if (event != null && !TextUtils.isEmpty(event.body)) {

            Description = event.description;
            EncryptConsume = event.EncryptConsumeTime;
            beforeLength = event.beforeLength;
            afterLength = event.afterLength;
            plainText = event.plainText;
            cipherText = event.cipherText;
//            beforeSendTime = System.currentTimeMillis();
            beforeSendTime = System.nanoTime();
            Transactions transactions = new Transactions(this);
            transactions.sendMessage(event.body, contact.getmNumber());
//            composeEditText.setText("");
        }
    }


    /**
     * 统计相关
     */
    private String Description = "";  //加密or没加密
    private long EncryptConsume = 0; //加密消耗时间
    private String beforeLength = "0"; //明文原长度
    private String afterLength = "0"; //加密后长度
    private long beforeSendTime = 0;  //发送前的时间
    private long afterSendTime = 0;  //发送后的时间
    private String plainText = ""; //明文
    private String cipherText = ""; //密文


    public void onEventMainThread(NotifySentSuccessEvent event) {
        if (event != null) {
            Debug.log(getPageName(), "更新数据");

            if (event.time > 0) {
                // TODO: 3/20/16  统计发送时间
                afterSendTime = event.time;
                long sendTime = afterSendTime - beforeSendTime;
                Debug.log("统计", "各项统计: \n" +
                        "方法: " + Description + "\n" +
                        "明文字符串: " + plainText + "\n" +
                        "明文字符串长度: " + beforeLength + "\n" +
                        "密文字符串(Base64后): " + cipherText + "\n" +
                        "密文字符串(Base64后)长度: " + afterLength + "\n" +
                        "加密消耗时间: " + String.valueOf(EncryptConsume) + "ns   约" + String.valueOf(EncryptConsume / 1000000.0f) + "ms \n" +
                        "发送消耗时间: " + String.valueOf(sendTime) + "ns   约" + String.valueOf(sendTime / 1000000.0f) + "ms \n" +
                        "总耗时: " + String.valueOf(EncryptConsume + sendTime) + "ns   约" + String.valueOf((EncryptConsume + sendTime) / 1000000.0f) + "ms \n"
                );

            }

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
        Intent intent = new Intent(context, MessageListActivity.class);
        intent.putExtra("conversation", conversation);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }


}
