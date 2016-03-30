package com.shootloking.secretmessager.view.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.model.Message;
import com.shootloking.secretmessager.task.AESDecryptAsyncTask;
import com.shootloking.secretmessager.task.RSADecryptAsyncTask;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.view.base.SMActivity;
import com.shootloking.secretmessager.view.dialog.MessageListAlertDialog;

/**
 * Created by shau-lok on 2/17/16.
 */
public class MessageListAdapter extends RecyclerCursorAdapter<MessageListAdapter.MessageListViewHolder, Message> {


    public static final String TAG = "MessageListAdapter";

    public static final int INCOMING_ITEM = 1;
    public static final int OUTGOING_ITEM = 2;


    String displayName;

    public MessageListAdapter(Context context) {
        this(context, "#");
    }

    public MessageListAdapter(Context context, String displayName) {
        super(context);
        this.displayName = !TextUtils.isEmpty(displayName) ? displayName : "#";
    }


    @Override
    public MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        int Resource;
        if (viewType == INCOMING_ITEM) {
            Resource = R.layout.list_item_message_in;
        } else {
            Resource = R.layout.list_item_message_out;
        }

        View view = inflater.inflate(Resource, parent, false);
        return new MessageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageListViewHolder holder, int position) {

        Message message = getItem(position);
        int textColor = context.getResources().getColor(R.color.colorBrownLight);
        TextDrawable drawable = TextDrawable.builder().
                beginConfig().
                textColor(Color.WHITE).fontSize(70).bold().toUpperCase().
                endConfig().
                buildRound(Character.toString(displayName.charAt(0)), textColor);

        holder.user_avatar.setImageDrawable(drawable);
        holder.mData = message;
        holder.message_content.setText(message.getBody());
        holder.message_date.setText(Utils.DateFormat(context, message.getDate()));
        holder.root.setOnClickListener(holder);
        holder.root.setOnLongClickListener(holder);
    }

    @Override
    protected Message getItem(int position) {
        cursor.moveToPosition(position);
        return Message.getMessage(context, cursor);
    }


    @Override
    public int getItemViewType(int position) {

        Message message = getItem(position);

        return message.getMsg_type();
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView user_avatar;
        TextView message_content;
        TextView message_date;
        View root;

        Message mData;


        public MessageListViewHolder(View itemView) {
            super(itemView);
            user_avatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            message_content = (TextView) itemView.findViewById(R.id.message_content);
            message_date = (TextView) itemView.findViewById(R.id.message_date);
            root = itemView;
        }

        @Override
        public void onClick(View v) {
            showDialog(displayName, mData);

        }

        @Override
        public boolean onLongClick(View v) {
            showDialog(displayName, mData);
            return true;
        }
    }


    private void showDialog(String title, final Message message) {
        FragmentManager fm = ((SMActivity) context).getFragmentManager();

        MessageListAlertDialog dialog = MessageListAlertDialog.newInstance(title);
        dialog.setmListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case MessageListAlertDialog.AES_DECRYPT_ITEM_TYPE: {
                        //AES解密

                        AESDecryptAsyncTask task = new AESDecryptAsyncTask((SMActivity) context);
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message.getBody());

                        break;
                    }
                    case MessageListAlertDialog.RSA_DECRYPT_ITEM_TYPE: {
                        //RSA解密
                        RSADecryptAsyncTask tasks = new RSADecryptAsyncTask((SMActivity) context);
                        tasks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message.getBody());

                        break;
                    }
                }
            }
        });
        dialog.show(fm, "messageDialog");
    }

}