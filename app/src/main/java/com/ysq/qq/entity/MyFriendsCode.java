package com.ysq.qq.entity;

import java.util.List;

public class MyFriendsCode {

    private String code;
    private String msg;
    private List<FriendsDTO> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<FriendsDTO> getData() {
        return data;
    }

    public void setData(List<FriendsDTO> data) {
        this.data = data;
    }
}
