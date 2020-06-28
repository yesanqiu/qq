package com.ysq.qq.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.ysq.qq.Index;
import com.ysq.qq.R;
import com.ysq.qq.adapter.IndexAdapter;
import com.ysq.qq.entity.Friends;
import com.ysq.qq.entity.FriendsDTO;
import com.ysq.qq.entity.MyFriendsCode;
import com.ysq.qq.entity.User;
import com.ysq.qq.utils.HttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IndexTask extends AsyncTask<Object,Integer, Boolean> {

    List<FriendsDTO> users = new ArrayList<>();
    ListView listView;
    Context context;
    boolean success = false;
    // 方法1：onPreExecute（）
    // 作用：执行 线程任务前的操作
    @Override
    protected void onPreExecute() {
        // 执行前显示提示
    }


    // 方法2：doInBackground（）
    // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
    // 此处通过计算从而模拟“加载进度”的情况
    @Override
    protected Boolean doInBackground(Object... params) {
        listView = (ListView) params[0];
        context = (Context) params[1];
        HttpUtil.request("http://yesanqiu.top:25502/user/getMyFriends", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("get f");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("t");
                String body = response.body().string();
                System.out.println(body);
                MyFriendsCode myFriendsCode = HttpUtil.fromToJson(body, MyFriendsCode.class);
                success = (myFriendsCode.getCode().equals("200"));
                users = myFriendsCode.getData();


            }
        });

        return success;

    }

    // 方法3：onProgressUpdate（）
    // 作用：在主线程 显示线程任务执行的进度
    @Override
    protected void onProgressUpdate(Integer... progresses) {


    }

    // 方法4：onPostExecute（）
    // 作用：接收线程任务执行结果、将执行结果显示到UI组件
    @Override
    protected void onPostExecute(Boolean result) {
        // 执行完毕后，则更新UI
        IndexAdapter indexAdapter = new IndexAdapter(users, context,R.layout.index_friends_line);
        listView.setAdapter(indexAdapter);
    }

    // 方法5：onCancelled()
    // 作用：将异步任务设置为：取消状态
    @Override
    protected void onCancelled() {


    }
}
