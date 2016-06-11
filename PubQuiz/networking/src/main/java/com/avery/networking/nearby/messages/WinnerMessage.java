package com.avery.networking.nearby.messages;

public class WinnerMessage extends BaseMessage {

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
