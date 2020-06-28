package com.ysq.qq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ysq.qq.adapter.NewFriendsAdapter;
import com.ysq.qq.entity.NewFriendsCode;
import com.ysq.qq.entity.User;
import com.ysq.qq.utils.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewFriends extends AppCompatActivity {

    static int code = 0;
    private Handler handler = new Handler();
    private NewFriendsLister newFriendsLister = new NewFriendsLister();
    private NewFriendsAdapter newFriendsAdapter;

    public static NewFriends instance = null;
    private List<User> data = new ArrayList<>();


    class NewFriendsLister implements Runnable{

        @Override
        public void run() {
            if(code != 0){
                if(code == 1){
                    newFriendsAdapter.updateData(data);
                }
                if(code == 2){
                    new PromptDialog(NewFriends.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText("SUCCESS")
                            .setContentText("添加好友成功")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }else{
                handler.postDelayed(newFriendsLister,1000);
            }
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friends);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        instance = this;
        ImageView index = findViewById(R.id.new_to_index);
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewFriends.this,Index.class));
            }
        });

        if(newFriendsAdapter == null){
            newFriendsAdapter = new NewFriendsAdapter(data,NewFriends.this,R.layout.new_friends_line);
        }

        ListView listView = findViewById(R.id.new_list);
        listView.setAdapter(newFriendsAdapter);
        getNewFriends();
    }

    public void getNewFriends(){
        handler.postDelayed(newFriendsLister,1000);
        HttpUtil.request("http://yesanqiu.top:25502/user/getMyNewFriends", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println("getNewFriend:" + body);
                NewFriendsCode newFriendsCode = HttpUtil.fromToJson(body,NewFriendsCode.class);
                data = newFriendsCode.getData();
                code = 1;
            }
        });
    }

    public void show(){
        code = 2;
    }
}
