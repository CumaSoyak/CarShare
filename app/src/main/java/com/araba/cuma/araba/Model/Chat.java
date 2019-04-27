package com.araba.cuma.araba.Model;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class Chat {
    private String friendId;
    private String friendName;
    private String friendPhoto;
    private String fromCity;
    private String messageUuid;
    private String toCity;
    private String message;
    private String receiver;
    private String sender;

    public Chat() {
    }

    public Chat(String friendId, String friendName, String friendPhoto, String fromCity, String messageUuid, String toCity, String message, String receiver, String sender) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendPhoto = friendPhoto;
        this.fromCity = fromCity;
        this.messageUuid = messageUuid;
        this.toCity = toCity;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendPhoto() {
        return friendPhoto;
    }

    public void setFriendPhoto(String friendPhoto) {
        this.friendPhoto = friendPhoto;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getMessageUuid() {
        return messageUuid;
    }

    public void setMessageUuid(String messageUuid) {
        this.messageUuid = messageUuid;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
