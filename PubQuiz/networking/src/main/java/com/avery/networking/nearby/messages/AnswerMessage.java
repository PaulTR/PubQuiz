package com.avery.networking.nearby.messages;

/**
 * Created by mark on 6/10/16.
 */
public class AnswerMessage extends BaseMessage{

    public String answer;

    public AnswerMessage() {
        messageType = "answer";
    }
}
