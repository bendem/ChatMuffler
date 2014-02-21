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
        distanceFromRadius = sender.getLocation().distance(receiver.getLocation()) - Config.SafeRadius.getDouble();

        ChatMuffler.logger.fine("Distance from player :" + sender.getLocation().distance(receiver.getLocation()));
        ChatMuffler.logger.fine("Safe radius size :" + Config.SafeRadius.getDouble());

        if(shouldAddNoise()) {
            ChatMuffler.logger.fine("Noise addition");
            noise = distanceFromRadius * Config.NoisePerBlock.getDouble();
            noiseGenerator = new NoiseGenerator(
                noise, getMessageToSend(originalMessage),
                Config.RandomEffectReducer.getDouble(),
                Config.KeepSpaces.getBoolean(),
                Config.ReplaceWith.getString()
            );
            messageToSend = noiseGenerator.getNoisifiedMessage();
        } else {
            messageToSend = getMessageToSend(originalMessage);
        }
    }

    public boolean shouldSend() {
        // TODO Refactor
        if(distanceFromRadius <= 0) { // Could it be !shouldAddNoise() ?
            ChatMuffler.logger.fine("Distance " + receiver.getDisplayName());
            return true;
        }
        if(noise > 1) {
            ChatMuffler.logger.fine("Too much noise " + receiver.getDisplayName());
            return false;
        }
        if(shouldAddNoise()) {
            if(noiseGenerator.getNbKeptChars() == 0
                    || (float) noiseGenerator.getNbKeptChars() / originalMessage.length()
                    < Config.RemainingCharsNeeded.getDouble()) {

                ChatMuffler.logger.fine("nbCharsKept " + receiver.getDisplayName());
                return false;
            }
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

        ChatMuffler.logger.fine("Default " + receiver.getDisplayName());
        return true;
    }

    public void send() {
        // TODO Format message (add MessageType symbol?)
        receiver.sendMessage("<" + sender.getDisplayName() + "> " + messageToSend);
        ChatMuffler.logger.fine(" -- SENT --");
        ChatMuffler.logger.fine("MessageType : " + messageType.name() + " :: " + sender.getDisplayName() + " => " + receiver.getDisplayName());
        ChatMuffler.logger.fine("Distance : " + distanceFromRadius + " :: Noise : " + noise);
        ChatMuffler.logger.fine(" -- /SENT --");
    }

    private MessageType getType() {
        if(messageType != null) {
            return messageType;
        }
        return getType(sender, originalMessage);
    }

    public static MessageType getType(Player sender, String message) {
        String messageSymbol;

        for(MessageType type : MessageType.values()) {
            messageSymbol = message.substring(0, type.getSymbol().length());

            if(messageSymbol.equals(type.getSymbol())
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
            return message.substring(getType().getSymbol().length());
        }
    }

    private boolean shouldAddNoise() {
        return messageType != MessageType.Global && distanceFromRadius > 0;
    }

    public MessageType getMessageType() {
        return messageType;
    }

}
