package com.ysq.qq.utils;

import java.net.URI;

public class WebSocketUtil {

    //WebSocket客户端
    private static JWebSocketClient jWebSocketClient;

    //用户id
    private static String id;

    //连接到服务器WebSocket
    public static void connect(String userId){
        URI uri = URI.create("ws://yesanqiu.top:25502/webSocket/" + userId);
        jWebSocketClient = new JWebSocketClient(uri);
        try {
            jWebSocketClient.connectBlocking();
            id = userId;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //发送信息
    public static void sendMessage(String oId,String msg){
        System.out.println("msg:" +msg
        );
        jWebSocketClient.send("#M:"+oId+":"+id+"#qq#" + msg);
    }

    //添加好友请求
    public static void addFriends(String fId){
        jWebSocketClient.send("#F:"+id+":"+fId);
    }

    //接受好友请求
    public static void accept(String fId){
        jWebSocketClient.send("#Y:"+id+":"+fId);
    }

    public static String getUserId(){
        return id;
    }
}
