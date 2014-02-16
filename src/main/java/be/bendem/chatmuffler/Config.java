package be.bendem.chatmuffler;

/**
 * Created by Ben on 15/02/14.
 */
public enum Config {

    NormalChatSymbol("chat-symbol.normal", null),
    ShoutChatSymbol("chat-symbol.shout", "+"),
    WhisperChatSymbol("chat-symbol.whisper", "-"),
    GlobalChatSymbol("chat-symbol.global", "#"),
    RemainingCharsNeeded("remaining-chars-percentage-needed", 0.3D),
    SafeRadius("safe-radius", 50D),
    NoisePerBlock("noise-per-block", 0.04);

    private String node;
    private Object defaultValue;

    Config(String configNode, Object defaultValue) {
        node = configNode;
        this.defaultValue = defaultValue;
    }

    public String getNode() {
        return node;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
