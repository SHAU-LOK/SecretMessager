package com.shootloking.secretmessager.view.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by shau-lok on 1/18/16.
 */
public abstract class SMActivity extends AppCompatActivity {

    protected abstract String getPageName();

    protected Resources mRes;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRes = getResources();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("加载中");
        mProgressDialog.setCancelable(false);
    }

    public void setContentView(int layRes) {
        super.setContentView(layRes);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }


    public Context getSelfContext() {
        return this;
    }

    public void mFinish() {
        finish();
    }

}
