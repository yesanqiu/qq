package com.ysq.qq.entity;

public class FriendsDTO {

    private User friends;
    private Message message;
    private boolean hasMessage;


    public User getUser() {
        return friends;
    }

    public void setUser(User user) {
        this.friends = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    @Override
    public String toString() {
        return "FriendsDTO{" +
                "friends=" + friends +
                ", message=" + message +
                ", hasMessage=" + hasMessage +
                '}';
    }
}
