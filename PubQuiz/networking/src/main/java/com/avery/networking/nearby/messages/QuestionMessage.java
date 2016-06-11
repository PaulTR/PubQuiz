package com.avery.networking.nearby.messages;

public class QuestionMessage extends BaseMessage {

    public String question;

    public QuestionMessage() {
        messageType = "question";
    }

    public QuestionMessage(String question) {
        this();
        this.question = question;
    }
}
