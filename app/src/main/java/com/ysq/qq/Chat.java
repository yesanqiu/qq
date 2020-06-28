package com.ysq.qq;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ysq.qq.adapter.ChatMsgAdapter;
import com.ysq.qq.entity.Message;
import com.ysq.qq.entity.MessageCode;
import com.ysq.qq.task.ChatMsgTask;
import com.ysq.qq.utils.HttpUtil;
import com.ysq.qq.utils.WebSocketUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Chat extends AppCompatActivity {

    ChatMsgTask chatMsgTask;

    ListView listView;

    String userId;
    static int code = 0;
    public static Chat instance = null;
    List<Message> messages = new ArrayList<>();

    private Handler handler = new Handler();
    private MsgLister msgLister = new MsgLister();
    private ChatMsgAdapter chatMsgAdapter = null;

    class MsgLister implements Runnable{

        @Override
        public void run() {
            if(code !=0){
                if(code == 1) {
                    chatMsgAdapter.updateData(messages);
                }
            }else {
                handler.postDelayed(msgLister,1000);
            }

        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        instance  = this;
        final Intent intent=getIntent();
        userId = intent.getStringExtra("userId");
        TextView name = findViewById(R.id.friends_name);
        name.setText(userId);

        ImageView iv = findViewById(R.id.chat_index);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chat.this,Index.class));
            }
        });

        TextView btn = findViewById(R.id.chat_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = findViewById(R.id.chat_msg);
                String msg = editText.getText().toString();
                editText.setText("");
                WebSocketUtil.sendMessage(userId,msg);
                initMsg();
            }
        });

        if(chatMsgAdapter == null){
            chatMsgAdapter = new ChatMsgAdapter(messages, Chat.this, R.layout.chat_msg_line, WebSocketUtil.getUserId());
        }
        listView = findViewById(R.id.chat_msg_list);
        listView.setAdapter(chatMsgAdapter);
        initMsg();
//        chatMsgTask = new ChatMsgTask();
//        chatMsgTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,listView,Chat.this,userId);

    }


    public void initMsg(){
        handler.postDelayed(msgLister,1000);
        HttpUtil.getMessage(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                System.out.println(body);
                MessageCode messageCode = HttpUtil.fromToJson(body, MessageCode.class);
                code = 1;
                messages = messageCode.getData();
            }
        },userId);
    }
}
