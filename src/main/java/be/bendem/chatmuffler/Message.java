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
    private final double      distanceFromRadius;
    private double         noise          = 0;
    private NoiseGenerator noiseGenerator = null;

    public Message(Player sender, Player receiver, String originalMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.originalMessage = originalMessage;
        messageType = getType();

        distanceFromRadius = sender.getLocation().distance(receiver.getLocation()) -
                ChatMuffler.config.getDouble(Config.SafeRadius.getNode(), (double) Config.SafeRadius.getDefaultValue());

        ChatMuffler.logger.info("Distance from player :" + sender.getLocation().distance(receiver.getLocation()));
        ChatMuffler.logger.info("Safe radius size :" + ChatMuffler.config.getDouble(Config.SafeRadius.getNode(), (double) Config.SafeRadius.getDefaultValue()));

        if(shouldAddNoise()) {
            ChatMuffler.logger.info("Noise addition");
            noise = distanceFromRadius * ChatMuffler.config.getDouble("noise-per-block", 0.05);
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
        if(distanceFromRadius <= 0) { // Could it be !shouldAddNoise() ?
            ChatMuffler.logger.info("Distance " + receiver.getDisplayName());
            return true;
        }
        if(noise > 1) {
            ChatMuffler.logger.info("Too much noise " + receiver.getDisplayName());
            return false;
        }
        if(shouldAddNoise() && noiseGenerator.getNbKeptChars() == 0
                || (float) noiseGenerator.getNbKeptChars() / originalMessage.length()
                < ChatMuffler.config.getDouble(Config.RemainingCharsNeeded.getNode(), 0.3)) {

            ChatMuffler.logger.info("nbCharsKept " + receiver.getDisplayName());
            return false;
        }
        // MessageType should have been converted to MessageType.Normal...
        if(messageType == MessageType.Global
                && (!sender.hasPermission(messageType.getPermission())
                || !receiver.hasPermission(messageType.getPermission()))) {
            ChatMuffler.logger.warning("- WARNING, This should not be happening - ");
            ChatMuffler.logger.warning("Permission problem, could you report this problem here : https://github.com/bendem/ChatMuffler/issues");
            ChatMuffler.logger.warning(messageType.name() + ", permission : " + messageType.getPermission());
            ChatMuffler.logger.warning("sender : " + sender.hasPermission(messageType.getPermission())
                    + ", receiver : " + receiver.hasPermission(messageType.getPermission()));
            return false;
        }

        ChatMuffler.logger.info("Default " + receiver.getDisplayName());
        return true;
    }

    public void send() {
        // TODO Format message (add MessageType symbol?)
        receiver.sendMessage("<" + sender.getDisplayName() + "> " + messageToSend);
        ChatMuffler.logger.info(" -- SENT --");
        ChatMuffler.logger.info("MessageType : " + messageType.name() + " :: " + sender.getDisplayName() + " => " + receiver.getDisplayName());
        ChatMuffler.logger.info("Distance : " + distanceFromRadius + " :: Noise : " + noise);
        ChatMuffler.logger.info(" -- /SENT --");
    }

    private MessageType getType() {
        if(messageType != null) {
            return messageType;
        }
        return getType(sender, originalMessage);
    }

    public static MessageType getType(Player sender, String message) {
        String firstChar = message.substring(0, 1);
        for(MessageType type : MessageType.values()) {
            if(firstChar.equals(ChatMuffler.config.getString(type.getConfigNode(), type.getDefaultValue()))
                    && sender.hasPermission(type.getPermission())
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
        return messageType != MessageType.Global && distanceFromRadius > 0;
    }

    public MessageType getMessageType() {
        return messageType;
    }

}
