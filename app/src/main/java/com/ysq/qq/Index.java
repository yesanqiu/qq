package com.ysq.qq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ysq.qq.adapter.IndexAdapter;
import com.ysq.qq.entity.Friends;
import com.ysq.qq.entity.FriendsDTO;
import com.ysq.qq.entity.MyFriendsCode;
import com.ysq.qq.task.IndexTask;
import com.ysq.qq.utils.HttpUtil;
import com.ysq.qq.utils.WebSocketUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Index extends AppCompatActivity {


    IndexTask indexTask;

    ListView listView;

    static int code = 0;
    public static Index instance = null;
    public List<FriendsDTO> friends = new ArrayList<>();

    private Handler handler = new Handler();

    public IndexAdapter indexAdapter = null;

    private IndexLister indexLister = new IndexLister();
    static List<String> newMessageUserIds = new ArrayList<>();

    class IndexLister implements Runnable{

        @Override
        public void run() {
            if(code !=0){
                if(code == 1) {
                    System.out.println("index adapter update");
                    System.out.println(friends.size());
                    System.out.println(friends.get(0).isHasMessage());
                    indexAdapter.updateData(friends);
                }
                if(code == 2){
                    ImageView view = findViewById(R.id.index_to_new);
                    view.setBackgroundResource(R.color.colorAccent);
                }
            }else {
                handler.postDelayed(indexLister,1000);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        instance = this;

        ImageView toAdd = findViewById(R.id.index_to_add);
        toAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Index.this,Add.class));
            }
        });

        ImageView toNew = findViewById(R.id.index_to_new);
        toNew.setBackgroundResource(R.color.colorBlue);
        toNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Index.this,NewFriends.class));
            }
        });


        if(indexAdapter == null){
            indexAdapter = new IndexAdapter(friends,Index.this,R.layout.index_friends_line);
        }
        listView = findViewById(R.id.friends_list);
        listView.setAdapter(indexAdapter);
        initFriends();
//        indexTask = new IndexTask();
//        indexTask.execute(listView,Index.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Index.this,Chat.class);
                intent.putExtra("userId",Long.toString(l));
                startActivity(intent);
            }
        });


    }

    public void initFriends(){
        handler.postDelayed(indexLister,1000);

        HttpUtil.request("http://yesanqiu.top:25502/user/getMyFriends", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                MyFriendsCode myFriendsCode = HttpUtil.fromToJson(body, MyFriendsCode.class);
                friends = myFriendsCode.getData();
                code = 1;
            }
        });

    }

    public void onMessage(){
        handler.postDelayed(indexLister,1000);
        code = 1;
    }

    public void onFriends(){
        handler.postDelayed(indexLister,1000);
        code = 2;
    }



}
