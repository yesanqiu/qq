package com.ysq.qq.entity;

public class Friends {

    private FriendsDTO friendsDTO;

    private boolean hasMessage;

    public Friends(){

    }

    public Friends(FriendsDTO friendsDTO,boolean hasMessage){
        this.friendsDTO = friendsDTO;
        this.hasMessage = hasMessage;
    }

    public Friends(FriendsDTO friendsDTO){
        this.friendsDTO = friendsDTO;
        this.hasMessage = false;
    }

    public FriendsDTO getFriendsDTO() {
        return friendsDTO;
    }

    public void setFriendsDTO(FriendsDTO friendsDTO) {
        this.friendsDTO = friendsDTO;
    }

    public boolean isHasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "friendsDTO=" + friendsDTO +
                ", hasMessage=" + hasMessage +
                '}';
    }
}
