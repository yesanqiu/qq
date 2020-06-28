package com.ysq.qq.entity;

import java.util.Date;

public class Message {

    private Integer mid;

    private String sid;

    private String oid;

    private String msg;

    private String time;

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", sid='" + sid + '\'' +
                ", oid='" + oid + '\'' +
                ", msg='" + msg + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Message(){}

    public Message(String msg){
        this.msg = msg;
    }
}
