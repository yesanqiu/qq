package com.ysq.qq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ysq.qq.entity.RegisterCode;
import com.ysq.qq.entity.StateCode;
import com.ysq.qq.entity.User;
import com.ysq.qq.utils.HttpUtil;

import java.io.IOException;

import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    static int code = 0;
    static String userId;

    private Handler handler = new Handler();
    private CodeLister codeLister = new CodeLister();

    class CodeLister implements Runnable{

        @Override
        public void run() {
            if(code !=0){
                if(code == 1) {
                    new PromptDialog(Register.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                            .setAnimationEnable(true)
                            .setTitleText("SUCCESS")
                            .setContentText("注册成功")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Register.this,Login.class);
                                    intent.putExtra("userId",userId);
                                    startActivity(intent);
                                }
                            }).show();

                }
                if(code == 2){
                    new PromptDialog(Register.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                            .setAnimationEnable(true)
                            .setTitleText("Wrong")
                            .setContentText("注册失败（检查一下密码是否小于6个字符）")
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
        setContentView(R.layout.register);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }



        Button button = findViewById(R.id.register_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(codeLister,1000);
                EditText psw = findViewById(R.id.psw);
                EditText secondePsw = findViewById(R.id.second_psw);
                String psw_text = psw.getText().toString();
                String second_psw_text = secondePsw.getText().toString();
                if(psw_text.equals(second_psw_text)){
                    HttpUtil.post("http://yesanqiu.top:25502/user/register", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.println("F");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            System.out.println("T");
                            String body = response.body().string();
                            System.out.println(body);
                            RegisterCode registerCode = HttpUtil.fromToJson(body, RegisterCode.class);
                            if("200".equals(registerCode.getCode())){
                                System.out.println("注册成功");
                                code = 1;
                                System.out.println(registerCode.getData().getUserId());
                                userId = registerCode.getData().getUserId();
                            }else{
                                System.out.println("注册失败");
                                code = 2;
                            }

                        }
                    },psw_text);
                }else {
                    new PromptDialog(Register.this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                            .setAnimationEnable(true)
                            .setTitleText("Error")
                            .setContentText("密码不一致")
                            .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }

                if(code == 0){
                    System.out.println(code);
                }
            }
        });
    }
}
