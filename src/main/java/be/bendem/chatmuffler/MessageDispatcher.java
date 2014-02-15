package be.bendem.chatmuffler;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Ben on 15/02/14.
 */
public class MessageDispatcher {

    private String originalMessage;
    private Player sender;
    private int    targetCount;

    public MessageDispatcher(String originalMessage, Player sender) {
        this.originalMessage = originalMessage;
        this.sender = sender;
    }

    public void dispatch() {
        targetCount = 0;
        for(Player receiver : getTargets()) {
            if(shouldSendMessageTo(receiver)) {
                receiver.sendMessage(getNoisifiedMessage());
                ++targetCount;
            }
        }
    }

    private boolean shouldSendMessageTo(Player receiver) {
        // TODO implement
        return false;
    }

    private ArrayList<Player> getTargets() {
        // TODO Implement
        return new ArrayList<>();
    }

    private String getNoisifiedMessage() {
        // TODO Implement
        return null;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public int getTargetCount() {
        return targetCount;
    }

}
