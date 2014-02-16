package be.bendem.chatmuffler;

import org.bukkit.entity.Player;

/**
 * Created by Ben on 16/02/14.
 */
public class Message {

    private final Player      sender;
    private final Player      receiver;
    private final MessageType messageType;
    private final String      originalMessage;
    private final String      messageToSend;
    private final double      distance;
    private double         noise = 0;
    private NoiseGenerator noiseGenerator = null;

    public Message(Player sender, Player receiver, String originalMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.originalMessage = originalMessage;
        messageType = getType();

        distance = sender.getLocation().distance(receiver.getLocation()) - ChatMuffler.config.getInt("radius", 20);
        if(shouldAddNoise()) {
            noise = distance * ChatMuffler.config.getDouble("noise-per-block", 0.05);
            noiseGenerator = new NoiseGenerator(
                noise, getMessageToSend(originalMessage),
                ChatMuffler.config.getDouble("random-effect-reducer", 0.5),
                ChatMuffler.config.getBoolean("keep-spaces", true),
                ChatMuffler.config.getString("replace-with", "..")
            );
            messageToSend = noiseGenerator.getNoisifiedMessage();
        } else {
            messageToSend = getMessageToSend(originalMessage);
        }
    }

    public boolean shouldSend() {
        // TODO Refactor
        if(distance <= 0) { // Could it be !shouldAddNoise() ?
            return true;
        }
        if(noise > 1) {
            return false;
        }
        if(messageType == MessageType.Global) {
            return true;
        }
        if(noiseGenerator.getNbKeptChars() == 0
                || (float) noiseGenerator.getNbKeptChars() / originalMessage.length()
                < ChatMuffler.config.getDouble(Config.RemainingCharsNeeded.getNode(), 0.3)) {
            return false;
        }

        return false;
    }

    public void send() {
        // TODO Format message (add "[sender]", add symbol?)
        receiver.sendMessage(originalMessage);
    }

    private MessageType getType() {
        // TODO Permission check!
        if(messageType != null) {
            return messageType;
        }
        String firstChar = originalMessage.substring(0, 1);
        for(MessageType type : MessageType.values()) {
            if(firstChar.equals(ChatMuffler.config.getString(type.getConfigNode(), type.getDefaultValue()))
                    && !type.equals(MessageType.Normal)) {
                return type;
            }
        }
        return MessageType.Normal;
    }

    private String getMessageToSend(String message) {
        if(getType() == MessageType.Normal) {
            return message;
        } else {
            return message.substring(ChatMuffler.config.getString(getType().getConfigNode(), getType().getDefaultValue()).length());
        }
    }

    private boolean shouldAddNoise() {
        return messageType != MessageType.Global && distance > 0;
    }

}
