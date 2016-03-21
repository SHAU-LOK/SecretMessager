package com.shootloking.secretmessager.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

/**
 * Created by shau-lok on 3/7/16.
 */
public class MessageListAlertDialog extends DialogFragment {

    ArrayAdapter<String> adapter;
    AlertDialog.Builder builder;
    DialogInterface.OnClickListener mListener;
    //    public static final String[] ARRAY_ITEM = new String[]{"加密信息", "解密信息", "复制信息", "删除信息"};
//    public static final String[] ARRAY_ITEM = new String[]{"加密信息", "解密信息", "复制信息"};
    public static final String[] ARRAY_ITEM = new String[]{"AES解密信息", "RSA解密信息"};


    /**
     * AES解密信息
     */
    public static final int AES_DECRYPT_ITEM_TYPE = 0;

    /**
     * RSA解密信息
     */
    public static final int RSA_DECRYPT_ITEM_TYPE = 1;


    public MessageListAlertDialog() {
    }


    public static MessageListAlertDialog newInstance(String title) {
        MessageListAlertDialog dialog = new MessageListAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(ARRAY_ITEM, mListener);


        return builder.create();

    }

    public DialogInterface.OnClickListener getmListener() {
        return mListener;
    }

    public void setmListener(DialogInterface.OnClickListener mListener) {
        this.mListener = mListener;
    }


}
