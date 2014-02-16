package be.bendem.chatmuffler;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Ben on 15/02/14.
 */
public class MessageDispatcher {

    private String messageToSend;
    private Player sender;
    private int    targetCount;

    public MessageDispatcher(String messageToSend, Player sender) {
        this.messageToSend = messageToSend;
        this.sender = sender;
    }

    public MessageDispatcher(String messageToSend, Player sender, MessageType messageType) {

    }

    public void dispatch() {
        Message message;
        targetCount = 0;

        for(Player receiver : getTargets()) {
            message = new Message(sender, receiver, messageToSend);
            if(message.shouldSend()) {
                message.send();
                ++targetCount;
            }
        }
    }

    private ArrayList<Player> getTargets() {
        // TODO Implement
        return new ArrayList<>();
    }

    private String getNoisifiedMessage() {
        // TODO Implement
        return null;
    }

    public String getMessageToSend() {
        return messageToSend;
    }

    public void setMessageToSend(String messageToSend) {
        this.messageToSend = messageToSend;
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
