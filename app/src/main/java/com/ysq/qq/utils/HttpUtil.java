package com.ysq.qq.utils;



import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static Gson gson = new Gson();
    private static OkHttpClient client = new OkHttpClient();
    private static String JSESSIONID;

    /**
     * 发送 URL 请求
     *
     * @param url
     * @return
     */
    public static void request(final String url, final Callback callback) {
        Request request = new Request.Builder().url(url).addHeader("cookie",JSESSIONID).addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").build();
        client.newCall(request).enqueue(callback);
    }


    public static void post(final String url,final Callback callback,final String psw){
        FormBody formBody = new FormBody.Builder().add("password",psw)
                .build();
        Request request = new Request.Builder().url(url).addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public static void getMessage(final Callback callback,final String fid){
        Request request = new Request.Builder().url("http://yesanqiu.top:25502/user/getMessage?fId=" +fid).addHeader("cookie",JSESSIONID).addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").get().build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public static void login(final Callback callback,final String username,final String psw){
        FormBody formBody = new FormBody.Builder()
                .add("userId",username)
                .add("password",psw)
                .build();
        Request request = new Request.Builder().url("http://yesanqiu.top:25502/user/login").addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)").post(formBody).build();
        Call call = client.newCall(request);
        call.enqueue(callback);

    }

    public static <T> T fromToJson(String json, Type listType) {
        T t = null;
        t = gson.fromJson(json, listType);
        return t;
    }


    public static String getJSESSIONID() {
        return JSESSIONID;
    }

    public static void setJSESSIONID(String JSESSIONID) {
        HttpUtil.JSESSIONID = JSESSIONID;
    }
}