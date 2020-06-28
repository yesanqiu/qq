package com.ysq.qq.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.ysq.qq.R;
import com.ysq.qq.adapter.ChatMsgAdapter;
import com.ysq.qq.entity.Message;
import com.ysq.qq.entity.MessageCode;
import com.ysq.qq.entity.User;
import com.ysq.qq.utils.HttpUtil;
import com.ysq.qq.utils.WebSocketUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChatMsgTask extends AsyncTask<Object,Integer,Boolean> {

    List<Message> messages = new ArrayList<>();
    ListView listView;
    Context context;
    boolean success = false;

    @Override
    protected Boolean doInBackground(Object[] objects) {
        listView = (ListView) objects[0];
        context = (Context) objects[1];
        String fId = (String) objects[2];
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
                success = (messageCode.getCode().equals("200"));
                messages = messageCode.getData();
            }
        },fId);
        return success;
    }


    @Override
    protected void onPostExecute(Boolean result) {
        // 执行完毕后，则更新UI
        ChatMsgAdapter chatMsgAdapter = new ChatMsgAdapter(messages, context, R.layout.chat_msg_line, WebSocketUtil.getUserId());
        listView.setAdapter(chatMsgAdapter);
    }
}
