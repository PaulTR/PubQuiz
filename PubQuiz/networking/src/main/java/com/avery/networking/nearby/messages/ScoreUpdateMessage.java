package com.avery.networking.nearby.messages;

import java.io.Serializable;

/**
 * Created by mark on 6/11/16.
 */
public class ScoreUpdateMessage extends BaseMessage implements Serializable {

    public String teamName;
    public int score;

    public ScoreUpdateMessage(String teamName, int score) {
        messageType = "score-update";
        this.teamName = teamName;
        this.score = score;
    }

}
