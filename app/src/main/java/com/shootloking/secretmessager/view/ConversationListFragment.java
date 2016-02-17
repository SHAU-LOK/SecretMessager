package com.shootloking.secretmessager.view;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.shootloking.secretmessager.utility.RecycleViewSpacingDecoration;
import com.shootloking.secretmessager.utility.log.Debug;
import com.shootloking.secretmessager.view.adapter.ConversationListAdapter;
import com.shootloking.secretmessager.view.base.SMApplication;
import com.shootloking.secretmessager.view.base.SMFragment;

import butterknife.Bind;

/**
 * Created by shau-lok on 1/31/16.
 */
public class ConversationListFragment extends SMFragment {

    @Bind(R.id.recycleView)
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

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

        initAdapter();
    }

    private void initAdapter() {
        if (!SMApplication.hasPermission(getActivity(), Manifest.permission.READ_SMS)) {
            SMApplication.requestPermission(getActivity(), Manifest.permission.READ_SMS, SMApplication.REQUEST_CODE_READ_SMS_PERMISSIONS);
        }
        if (!SMApplication.hasPermission(getActivity(), Manifest.permission.READ_CONTACTS)) {
            SMApplication.requestPermission(getActivity(), Manifest.permission.READ_CONTACTS, SMApplication.REQUEST_CODE_READ_CONTACT_PERMISSIONS);
        }
        ConversationListAdapter adapter = new ConversationListAdapter(getActivity());
//        ConversationAsyncHelper.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewSpacingDecoration(40));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case SMApplication.REQUEST_CODE_READ_SMS_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Debug.log(getClassName(), "授权信息成功");
//                    Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
                    initAdapter();
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
                } else {
                    Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
