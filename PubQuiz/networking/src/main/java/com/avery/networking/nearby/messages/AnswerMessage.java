package com.avery.networking.nearby.messages;

/**
 * Created by mark on 6/10/16.
 */
public class AnswerMessage extends BaseMessage{

    public String answer;
    public String answerType;

    public AnswerMessage() {
        messageType = "answer";
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
