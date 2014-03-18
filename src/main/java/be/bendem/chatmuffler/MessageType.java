package be.bendem.chatmuffler;

/**
 * Created by Ben on 15/02/14.
 */
public enum MessageType {

    Normal(Config.NormalChatSymbol, null, null),
    Shout(Config.ShoutChatSymbol, Config.ShoutRadiusModifier, "chatmuffler.shout"),
    Whisper(Config.WhisperChatSymbol, Config.WhisperRadiusModifier, "chatmuffler.whisper"),
    Global(Config.GlobalChatSymbol, null, "chatmuffler.global");

    private final String configNode;
    private final String symbol;
    private final String permission;
    private final double radiusModifier;

    MessageType(Config symbolConfig, Config radiusModifierConfig, String permission) {
        configNode = symbolConfig.getNode();
        symbol = symbolConfig.getString();
        this.permission = permission;
        if(radiusModifierConfig != null) {
            radiusModifier = radiusModifierConfig.getDouble();
        } else {
            radiusModifier = 0;
        }
    }

    public String getConfigNode() {
        return configNode;
    }

    public String getSymbol() {
        return symbol == null ? "" : symbol;
    }

    public String getPermission() {
        return permission;
    }

    public double getRadiusModifier() {
        return radiusModifier;
    }

    public String getReceiverPermission() {
        return permission + ".receive";
    }

    public String getSenderPermission() {
        return permission + ".send";
    }
}
