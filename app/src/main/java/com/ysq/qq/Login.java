package com.ysq.qq;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ysq.qq.entity.StateCode;
import com.ysq.qq.utils.HttpUtil;
import com.ysq.qq.utils.WebSocketUtil;


import java.io.IOException;

import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    static int code = 0;
    static String userId;
    private Handler handler = new Handler();
    private CodeLister codeLister = new CodeLister();

    class CodeLister implements Runnable{

        @Override
        public void run() {
            if(code !=0){
                if(code == 1) {
                    new PromptDialog(Login.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText("SUCCESS")
                            .setContentText("登录成功")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                    WebSocketUtil.connect(userId);
                                    startActivity(new Intent(Login.this,Index.class));
                                }
                            }).show();

                }
                if(code == 2){
                    new PromptDialog(Login.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                            .setAnimationEnable(true)
                            .setTitleText("Wrong")
                            .setContentText("登录失败")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }else{
                handler.postDelayed(codeLister,1000);
            }
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        code = 0;


        Intent intent=getIntent();
        final String userId = intent.getStringExtra("userId");
        TextView username = findViewById(R.id.login_username);
        username.setText(userId);

        TextView toRegister = findViewById(R.id.to_register);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        Button button = findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(codeLister,1000);
                EditText username = findViewById(R.id.login_username);
                final String username_str = username.getText().toString();
                EditText password = findViewById(R.id.login_password);
                String password_str = password.getText().toString();
                System.out.println(username_str);
                System.out.println(password_str);
                HttpUtil.login(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("F");
                        code = 2;
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("T");
                        String body = response.body().string();
                        String session = response.headers().get("Set-Cookie");
                        if (session != null){
                            String JSESSIONID = session.substring(0, session.indexOf(";"));
                            HttpUtil.setJSESSIONID(JSESSIONID);
                            System.out.println("JSESSIONID");
                            System.out.println(JSESSIONID);
                        }
                        StateCode stateCode = HttpUtil.fromToJson(body, StateCode.class);
                        System.out.println(stateCode.getCode());
                        if(stateCode.getCode().equals("200")){
                            code = 1;
                            Login.userId = username_str;
                            System.out.println("登录成功");
                        }else {
                            code = 2;
                            System.out.println("登陆失败");
                        }
                    }
                },username_str,password_str);
            }
        });
    }
}
