package com.avery.networking.nearby.messages;

import java.io.Serializable;

public class WinnerMessage extends BaseMessage implements Serializable {

    public String winner;

    public WinnerMessage() {
        messageType = "winner";
    }

    public WinnerMessage(String winner) {
        this();
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

}
