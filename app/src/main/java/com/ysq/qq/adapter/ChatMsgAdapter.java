package com.ysq.qq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysq.qq.R;
import com.ysq.qq.entity.Message;
import com.ysq.qq.entity.User;

import java.util.List;

public class ChatMsgAdapter extends BaseAdapter {

    private List<Message> msgs;
    private Context context;
    private int resource;
    private String mId;

    public ChatMsgAdapter(List<Message> msgs,Context context,int resource,String mId){
        this.msgs = msgs;
        this.context = context;
        this.resource = resource;
        this.mId = mId;
    }

    public void updateData(List<Message> list) {

        if(null == list) return;

        msgs.clear();

        msgs.addAll(list);

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int i) {
        return msgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return msgs.get(i).getMid();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(resource,viewGroup,false);
        Message m = msgs.get(i);
        System.out.println(mId);
        System.out.println(m.getSid());
        System.out.println(mId.equals(m.getSid()));
        if(mId.equals(m.getSid())) {
            LinearLayout linearLayout = view.findViewById(R.id.chat_friend_msg_line);
            linearLayout.setVisibility(View.GONE);
            TextView msg = view.findViewById(R.id.chat_mine_msg_line_msg);
            msg.setText(m.getMsg());
        }else{
            LinearLayout linearLayout = view.findViewById(R.id.chat_mine_msg_line);
            linearLayout.setVisibility(View.GONE);
            TextView msg = view.findViewById(R.id.chat_friend_msg_line_msg);
            msg.setText(m.getMsg());
        }

        return view;
    }
}
