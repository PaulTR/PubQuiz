package com.avery.networking.nearby.messages;

/**
 * Created by mark on 6/10/16.
 */
public class RegisterResponseMessage extends BaseMessage {

    public boolean isSuccessful = false;

    public RegisterResponseMessage() {
        messageType = "registerResponse";
    }
}
