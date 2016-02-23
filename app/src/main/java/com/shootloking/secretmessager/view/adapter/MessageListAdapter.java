package com.shootloking.secretmessager.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shootloking.secretmessager.R;
import com.shootloking.secretmessager.model.Message;
import com.shootloking.secretmessager.utility.Utils;
import com.shootloking.secretmessager.utility.log.Debug;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by shau-lok on 2/17/16.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {


    public static final String TAG = "MessageListAdapter";

    public static final int INCOMING_ITEM = 1;
    public static final int OUTGOING_ITEM = 2;


    String displayName;

    Context context;
    Cursor cursor;
    Uri uri;
    public ArrayList<Message> mMessages;
    int threadId;

    public MessageListAdapter(Context context, Uri uri) {
        this.context = context;
        cursor = null;
        this.uri = uri;

        threadId = -1;
        if (uri == null || uri.getLastPathSegment() == null) {
            threadId = -1;
        } else {
            threadId = Integer.parseInt(uri.getLastPathSegment());
        }

//        Conversation conversation = Conversation.getConversation(context, threadId);
//        Contact contact = conversation.getContact();
//        String address = contact.getmNumber();
//        String name = contact.getDisplayName();

        mMessages = Message.getMessageArrayList(context, threadId, uri);

        Debug.log(TAG, "messages list size: " + mMessages.size());
    }

    /**
     * 刷新数据
     */
    public void updateResource() {
        Observable.just(Message.getMessageArrayList(context, threadId, uri))
                .subscribe(new Action1<ArrayList<Message>>() {
                    @Override
                    public void call(ArrayList<Message> messages) {
                        mMessages = messages;
                        if (messages != null)
                            notifyDataSetChanged();
                    }
                });

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

        Message message = mMessages.get(position);
        holder.mData = message;
        holder.message_content.setText(message.getBody());
        holder.message_date.setText(Utils.DateFormat(context, message.getDate()));
        holder.root.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mMessages != null && mMessages.size() > 0 ? mMessages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        Message message = mMessages.get(position);

        return message.getMsg_type();
    }


    public class MessageListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        }
    }


}