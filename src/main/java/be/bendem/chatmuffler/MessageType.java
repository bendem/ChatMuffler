package be.bendem.chatmuffler;

/**
 * Created by Ben on 15/02/14.
 */
public enum MessageType {

    Normal(Config.NormalChatSymbol, null),
    Shout(Config.ShoutChatSymbol, "chatmuffler.shout"),
    Whisper(Config.WhisperChatSymbol, "chatmuffler.whisper"),
    Global(Config.GlobalChatSymbol, "chatmuffler.global");

    private final String configNode;
    private final String defaultValue;
    private final String permission;

    MessageType(Config config, String permission) {
        configNode = config.getNode();
        defaultValue = (String) config.getDefaultValue();
        this.permission = permission;
    }

    public String getConfigNode() {
        return configNode;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getPermission() {
        return permission;
    }

}
