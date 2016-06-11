package com.avery.networking.deserialize;

import android.util.Log;

import com.avery.networking.nearby.messages.AnswerMessage;
import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.networking.nearby.messages.QuestionMessage;
import com.avery.networking.nearby.messages.RegisterMessage;
import com.avery.networking.nearby.messages.RegisterResponseMessage;
import com.avery.networking.nearby.messages.WinnerMessage;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by mark on 6/10/16.
 */
public class BaseMessageDeserializer implements JsonDeserializer<BaseMessage> {

    private static final String TAG = BaseMessageDeserializer.class.getSimpleName();

    @Override
    public BaseMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Log.e(TAG, json.toString());
        String messageType = json.getAsJsonObject().get("messageType").getAsString();

        Log.e(TAG, "TYPE : " + messageType);
        BaseMessage message = null;
        if("register".equalsIgnoreCase(messageType)) {
            RegisterMessage registerMessage = new RegisterMessage();
            registerMessage.teamName = json.getAsJsonObject().get("teamName").getAsString();
            return registerMessage;
        }else if("registerResponse".equalsIgnoreCase(messageType)) {
            RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage();
            registerResponseMessage.isSuccessful = json.getAsJsonObject().get("isSuccessful").getAsBoolean();
            return registerResponseMessage;
        }else if("answer".equalsIgnoreCase(messageType)) {
            AnswerMessage answerMessage = new AnswerMessage();
            answerMessage.answer = json.getAsJsonObject().get("answer").getAsString();
            return answerMessage;
        }else if( "question".equalsIgnoreCase(messageType) ) {
            QuestionMessage questionMessage = new QuestionMessage();
            questionMessage.questionType = json.getAsJsonObject().get("questionType").getAsString();
            if( "multiple-choice".equalsIgnoreCase(questionMessage.questionType)) {
                questionMessage.question = json.getAsJsonObject().get("question").getAsString();
                questionMessage.A = json.getAsJsonObject().get("A").getAsString();
                questionMessage.B = json.getAsJsonObject().get("B").getAsString();
                questionMessage.C = json.getAsJsonObject().get("C").getAsString();
                questionMessage.D = json.getAsJsonObject().get("D").getAsString();
            }
            return questionMessage;
        } else if( "winner".equalsIgnoreCase(messageType) ) {
            WinnerMessage winnerMessage = new WinnerMessage();
            winnerMessage.winner = json.getAsJsonObject().get("winner").toString();
            return winnerMessage;
        } else {
            message = new BaseMessage();
            message.messageType = messageType;
            return message;
        }
    }
}
