package be.bendem.chatmuffler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 15/02/14.
 */
public class MessageDispatcher {

    private HashSet<Player> recipients = new HashSet<>();
    private String messageToSend;
    private Player sender;
    private int    targetCount;

    public MessageDispatcher(String messageToSend, Player sender, Set<Player> recipients) {
        this.messageToSend = messageToSend;
        this.sender = sender;
        this.recipients.addAll(recipients);
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

    private HashSet<Player> getTargets() {
        if(Message.getType(sender, messageToSend) == MessageType.Global) {
            return recipients;
        }

        HashSet<Player> targets = new HashSet<>();
        double boxSize;
        boxSize  = 1.0 / Config.NoisePerBlock.getDouble();
        boxSize += Config.SafeRadius.getDouble();

        ChatMuffler.logger.fine("box size : " + boxSize);
        List<Entity> nearbyEntities = sender.getNearbyEntities(boxSize, boxSize, boxSize);
        for(Entity entity : recipients) {
            if(entity instanceof Player && nearbyEntities.contains(entity)) {
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
