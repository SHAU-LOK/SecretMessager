package com.shootloking.secretmessager.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.data.Constants;
import com.shootloking.secretmessager.event.NotifyReceiveEvent;
import com.shootloking.secretmessager.event.NotifySentSuccessEvent;
import com.shootloking.secretmessager.model.Conversation;
import com.shootloking.secretmessager.utility.RecycleViewSpacingDecoration;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.adapter.ConversationListAdapter;
import com.shootloking.secretmessager.view.base.SMApplication;
import com.shootloking.secretmessager.view.base.SMFragment;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by shau-lok on 1/31/16.
 */
public class ConversationListFragment extends SMFragment {

    @Bind(R.id.recycleView)
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;
    ConversationListAdapter adapter;

    Subscription mSubscription;


    @Override
    protected String getClassName() {
        return "ConversationListFragment";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversionlist);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        EventBus.getDefault().register(this);

        adapter = new ConversationListAdapter(getSelfContext());
        recyclerView.addItemDecoration(new RecycleViewSpacingDecoration(50));
        recyclerView.setAdapter(adapter);

        initAdapter();


    }

    private void initAdapter() {

        if (!SMApplication.hasPermission(getActivity(), Manifest.permission.READ_SMS)) {
//            SMApplication.requestPermission(getActivity(), Manifest.permission.READ_SMS, SMApplication.REQUEST_CODE_READ_SMS_PERMISSIONS);
            //Fragment 跟 Activity 请求权限不一样
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, SMApplication.REQUEST_CODE_READ_SMS_PERMISSIONS);
        } else if (!SMApplication.hasPermission(getActivity(), Manifest.permission.READ_CONTACTS)) {
//            SMApplication.requestPermission(getActivity(), Manifest.permission.READ_CONTACTS, SMApplication.REQUEST_CODE_READ_CONTACT_PERMISSIONS);
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, SMApplication.REQUEST_CODE_READ_CONTACT_PERMISSIONS);
        } else {
            refresh();
        }
//        ConversationAsyncHelper.setAdapter(adapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case SMApplication.REQUEST_CODE_READ_SMS_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Debug.log(getClassName(), "授权信息成功");
//                    Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
                    initAdapter();
//                    refresh();
                } else {
                    Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case SMApplication.REQUEST_CODE_READ_CONTACT_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Debug.log(getClassName(), "授权联系人成功");
//                    Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
                    initAdapter();
//                    refresh();
                } else {
                    Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void onEventMainThread(NotifyReceiveEvent event) {
        // 收到信息
        refresh();
    }

    public void onEventMainThread(NotifySentSuccessEvent event) {
        //  发送信息成功
        refresh();
    }


    public void refresh() {
        final Observable<Cursor> myObservables = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                Cursor cursor = getSelfContext().getContentResolver().query(Constants.URI_SIMPLE, Conversation.PROJECTION, Conversation.COLUMN_COUNT + ">0", null, Conversation.SORT_ORDER);
//                Debug.log(getClassName(), DatabaseUtils.dumpCursorToString(cursor));
                if (Utils.isCursorValid(cursor)) {
                    subscriber.onNext(cursor);
//                    subscriber.onCompleted();
                }
            }
        });

        mSubscription = myObservables.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Cursor>() {
                    @Override
                    public void call(Cursor cursor) {
                        adapter.changeCursor(cursor);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
