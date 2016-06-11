package com.avery.networking.nearby.messages;

import java.io.Serializable;

public class QuestionMessage extends BaseMessage implements Serializable {

    public String question;

    public QuestionMessage() {
        messageType = "question";
    }

    public QuestionMessage(String question) {
        this();
        this.question = question;
    }
}
