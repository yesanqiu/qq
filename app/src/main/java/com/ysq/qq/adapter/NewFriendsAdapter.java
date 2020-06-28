package com.ysq.qq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysq.qq.Index;
import com.ysq.qq.NewFriends;
import com.ysq.qq.R;
import com.ysq.qq.entity.Message;
import com.ysq.qq.entity.User;
import com.ysq.qq.utils.WebSocketUtil;

import java.util.List;

public class NewFriendsAdapter extends BaseAdapter {

    private List<User> data;
    private Context context;
    private int resource;

    public NewFriendsAdapter (List<User> data,Context context,int resource){
        this.data = data;
        this.context = context;
        this.resource = resource;
        System.out.println(getCount());
    }

    public NewFriendsAdapter(){}

    public void updateData(List<User> list) {

        if(null == list) return;

        data.clear();

        data.addAll(list);

        notifyDataSetChanged();
        System.out.println(getCount());

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(data.get(i).getUserId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view  = LayoutInflater.from(context).inflate(resource,viewGroup,false);
        System.out.println("getview");
        final User user = data.get(i);
        TextView name = view.findViewById(R.id.new_friend_name);
        name.setText(user.getUserId());
        TextView accept = view.findViewById(R.id.new_accpet);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(user.getUserId());
                System.out.println("接受");
                WebSocketUtil.accept(user.getUserId());
                Index.instance.initFriends();
                NewFriends.instance.show();

            }
        });
        return view;
    }
}
