package com.avery.networking.nearby.messages;

/**
 * Created by mark on 6/10/16.
 */
public class RegisterMessage extends BaseMessage {

    public String teamName;

    public RegisterMessage() {
        messageType = "register";
    }
}
