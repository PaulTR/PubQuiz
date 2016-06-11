package com.avery.networking.nearby.messages;

import java.io.Serializable;

/**
 * Created by mark on 6/10/16.
 */
public class RegisterMessage extends BaseMessage implements Serializable {

    public String teamName;

    public RegisterMessage() {
        messageType = "register";
    }
}
