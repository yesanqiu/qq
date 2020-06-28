package com.ysq.qq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ysq.qq.entity.FindUserCode;
import com.ysq.qq.entity.FriendsDTO;
import com.ysq.qq.entity.User;
import com.ysq.qq.utils.HttpUtil;
import com.ysq.qq.utils.WebSocketUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Add extends AppCompatActivity {

    static int code = 0;

    private Handler handler = new Handler();
    private AddLister addLister = new AddLister();

    private User user;

    class AddLister implements Runnable{

        @Override
        public void run() {
            if(code !=0){
                if(code == 1) {
                    if(user != null) {
                        TextView name = findViewById(R.id.add_user_name);
                        name.setText(user.getUserId());
                        LinearLayout linearLayout = findViewById(R.id.add_user_msg_line);
                        linearLayout.setVisibility(View.VISIBLE);
                    }else{
                        new PromptDialog(Add.this)
                                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                .setAnimationEnable(true)
                                .setTitleText("SUCCESS")
                                .setContentText("查无此人")
                                .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(PromptDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }
            }else {
                handler.postDelayed(addLister,1000);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        ImageView toIndex = findViewById(R.id.add_to_index);
        toIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Add.this,Index.class));
            }
        });
        ImageView search = findViewById(R.id.add_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText search = findViewById(R.id.add_search_id);
                String searchId = search.getText().toString();
                getUser(searchId);
            }
        });
        TextView add = findViewById(R.id.add_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebSocketUtil.addFriends(user.getUserId());
                new PromptDialog(Add.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                        .setAnimationEnable(true)
                        .setTitleText("SUCCESS")
                        .setContentText("请求已发生，请等待对方答复")
                        .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    public void getUser(String fId){
        handler.postDelayed(addLister,1000);
        HttpUtil.request("http://yesanqiu.top:25502/user/findUser?userId=" + fId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                FindUserCode findUserCode = HttpUtil.fromToJson(body, FindUserCode.class);
                System.out.println(body);
                user = findUserCode.getData();
                code = 1;
            }
        });
    }
}
