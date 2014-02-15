package be.bendem.chatmuffler;

/**
 * Created by Ben on 15/02/14.
 */
public enum MessageType {

    Normal(Config.NormalChatSymbol),
    Shout(Config.ShoutChatSymbol),
    Whisper(Config.WhisperChatSymbol),
    Global;

    private final String configNode;

    private final String defaultValue;

    MessageType() {
        this(null);
    }

    MessageType(Config config) {
        configNode = config.getNode();
        defaultValue = (String) config.getDefaultValue();
    }

    public String getConfigNode() {
        return configNode;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

}
