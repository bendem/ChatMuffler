package be.bendem.chatmuffler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

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
        if(Message.getType(sender, messageToSend) == MessageType.Global) {
            return new ArrayList<>(Arrays.asList(Bukkit.getOnlinePlayers()));
        }

        ArrayList<Player> targets = new ArrayList<>();
        double boxSize;
        boxSize  = 1.0 / ChatMuffler.config.getDouble(Config.NoisePerBlock.getNode(), (double) Config.NoisePerBlock.getDefaultValue());
        boxSize += ChatMuffler.config.getDouble(Config.SafeRadius.getNode());

        ChatMuffler.logger.info("box size : " + boxSize);
        for(Entity entity : sender.getNearbyEntities(boxSize, boxSize, boxSize)) {
            if(entity instanceof Player) {
                targets.add((Player) entity);
            }
        }
        targets.add(sender); // Replicate default behavior

        return targets;
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
