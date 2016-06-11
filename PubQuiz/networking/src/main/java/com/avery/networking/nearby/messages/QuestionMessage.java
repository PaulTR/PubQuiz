package com.avery.networking.nearby.messages;

import com.avery.networking.model.Question;

public class QuestionMessage extends BaseMessage {

    public String question;
    public String A;
    public String B;
    public String C;
    public String D;

    public QuestionMessage() {
        messageType = "question";
    }

    public QuestionMessage(Question question) {
        this();
        this.question = question.getQuestion();
        this.A = question.getA();
        this.B = question.getB();
        this.C = question.getC();
        this.D = question.getD();
    }
}
