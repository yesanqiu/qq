package com.ysq.qq.adapter;

import android.content.Context;
import android.hardware.camera2.params.LensShadingMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.ysq.qq.R;
import com.ysq.qq.entity.Friends;
import com.ysq.qq.entity.FriendsDTO;
import com.ysq.qq.entity.User;

import java.util.List;

public class IndexAdapter extends BaseAdapter {

    private List<FriendsDTO> users;
    private Context context;
    private int resource;

    public List<FriendsDTO> getUsers() {
        return users;
    }

    public void setUsers(List<FriendsDTO> users) {
        this.users = users;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public IndexAdapter(List<FriendsDTO> users, Context context, int layout){
        this.users = users;
        this.context = context;
        this.resource = layout;

    }

    public IndexAdapter(){}

    public void updateData(List<FriendsDTO> list) {

        if(null == list){
            System.out.println("空");
            return;
        }

        users.clear();

        users.addAll(list);
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(users.get(i).getUser().getUserId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(resource,viewGroup,false);

        FriendsDTO u = users.get(i);

        TextView name = view.findViewById(R.id.index_friend_name);
        name.setText(u.getUser().getUserId());

        TextView msg = view.findViewById(R.id.index_friend_msg);
        System.out.println(u.getMessage());
        if(u.getMessage() != null){
            msg.setText(u.getMessage().getMsg());
        }
        System.out.println(u.getUser().getUserId());

        TextView hasMessage = view.findViewById(R.id.index_friend_has_message);
        if(u.isHasMessage()){
            hasMessage.setBackgroundResource(R.color.colorAccent);
        }
        return view;
    }
}
