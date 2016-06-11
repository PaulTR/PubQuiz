package com.avery.networking.deserialize;

import android.util.Log;

import com.avery.networking.nearby.messages.BaseMessage;
import com.avery.networking.nearby.messages.RegisterMessage;
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

        String messageType = json.getAsJsonObject().get("messageType").getAsString();

        Log.e(TAG, "TYPE : " + messageType);
        BaseMessage message = null;
        if(messageType == "register") {
            RegisterMessage registerMessage = new RegisterMessage();
            registerMessage.teamName = json.getAsJsonObject().get("teamName").getAsString();
            return registerMessage;
        }else {
            message = new BaseMessage();
            message.messageType = messageType;
            return message;
        }
    }
}
