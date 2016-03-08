package com.shootloking.secretmessager.view.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.encryption.EncryptManger;
import com.shootloking.secretmessager.model.Message;
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

    ArrayAdapter<String> adapter;

    //    Context context;
//    Cursor cursor;
//    Uri uri;
    //    public ArrayList<Message> mMessages;
//    int threadId;

    public MessageListAdapter(Context context) {
        super(context);
//        init(context);
//        this.uri = uri;

//        threadId = -1;
//        if (uri == null || uri.getLastPathSegment() == null) {
//            threadId = -1;
//        } else {
//            threadId = Integer.parseInt(uri.getLastPathSegment());
//        }

//        Conversation conversation = Conversation.getConversation(context, threadId);
//        Contact contact = conversation.getContact();
//        String address = contact.getmNumber();
//        String name = contact.getDisplayName();

//        mMessages = Message.getMessageArrayList(context, threadId, uri);

//        Debug.log(TAG, "messages list size: " + mMessages.size());
    }

    /**
     * 刷新数据
     */
    public void updateResource() {
//        Observable.just(Message.getMessageArrayList(context, threadId, uri))
//                .subscribe(new Action1<ArrayList<Message>>() {
//                    @Override
//                    public void call(ArrayList<Message> messages) {
//                        mMessages = messages;
//                        if (messages != null)
//                            notifyDataSetChanged();
//                    }
//                });

    }


    @Override
    public MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        int Resource;
        boolean send;
        if (viewType == INCOMING_ITEM) {
            Resource = R.layout.list_item_message_in;
            send = false;
        } else {
            Resource = R.layout.list_item_message_out;
            send = true;
        }

        View view = inflater.inflate(Resource, parent, false);
        MessageListViewHolder holder = new MessageListViewHolder(view);
        if (!send) {
            holder.user_avatar.setImageResource(R.mipmap.ic_person);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MessageListViewHolder holder, int position) {

//        Message message = mMessages.get(position);
        Message message = getItem(position);
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

//    @Override
//    public int getItemCount() {
//        return mMessages != null && mMessages.size() > 0 ? mMessages.size() : 0;
//    }

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
            Toast.makeText(context, "onclick", Toast.LENGTH_SHORT).show();
            showDialog(displayName, mData);

        }

        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(context, "on long click", Toast.LENGTH_SHORT).show();
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
                    case MessageListAlertDialog.ENCRYPT_ITEM_TYPE: {
                        //加密
                        try {
                            String cipher = EncryptManger.getInstance().Encrypt(message.getBody());
                            Toast.makeText(context, cipher, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "加密失败", Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case MessageListAlertDialog.DECRYPT_ITEM_TYPE: {
                        //解密
                        try {
                            String plain = EncryptManger.getInstance().Decrypt(message.getBody());
                            Toast.makeText(context, plain, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "解密失败", Toast.LENGTH_LONG).show();
                        }

                        break;
                    }
                    case MessageListAlertDialog.COPY_ITEM_TYPE: {
                        //复制
                        Toast.makeText(context, "复制功能尚未开发", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
            }
        });
        dialog.show(fm, "messageDialog");
    }

}