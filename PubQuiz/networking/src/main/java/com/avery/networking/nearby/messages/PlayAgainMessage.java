package com.avery.networking.nearby.messages;

/**
 * Created by mark on 6/11/16.
 */
public class PlayAgainMessage extends BaseMessage {

    public boolean isPlaying = false;

    public PlayAgainMessage(boolean isPlaying) {
        messageType = "play-again";
        this.isPlaying = isPlaying;
    }
}
