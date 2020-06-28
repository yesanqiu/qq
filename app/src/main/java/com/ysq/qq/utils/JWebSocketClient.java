package com.ysq.qq.utils;

import android.util.Log;
import android.widget.ImageView;


import com.ysq.qq.Chat;
import com.ysq.qq.Index;
import com.ysq.qq.R;
import com.ysq.qq.entity.Friends;
import com.ysq.qq.entity.FriendsDTO;
import com.ysq.qq.entity.Message;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;

public class JWebSocketClient extends WebSocketClient {


    public JWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("onOpen()");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("onMessage");
        System.out.println(message);
        sortMsg(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("onClose");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("onError");
        ex.printStackTrace();
    }

    public void sortMsg(String msg){
        String flag = msg.substring(1,2);
        if(flag.equals("M")){
            String message = msg.substring(msg.lastIndexOf("#qq#")+4,msg.length());
            String sId = msg.substring(14,msg.lastIndexOf("#qq#"));
            if(Index.instance != null){
                List<FriendsDTO> friendsDTOS = Index.instance.friends;
                for(FriendsDTO f: friendsDTOS){
                    if(f.getUser().getUserId().equals(sId)){
                        f.setMessage(new Message(message));
                        f.setHasMessage(true);
                    }
                }
                Index.instance.friends = friendsDTOS;
                Index.instance.onMessage();
            }

            if(Chat.instance != null){
                Chat.instance.initMsg();
            }
        }

        if(flag.equals("F")){
            Index.instance.onFriends();
        }

        if(flag.equals("Y")){
            Index.instance.initFriends();
        }
    }

}
