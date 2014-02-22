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
    SafeRadius("safe-radius", 30D),
    RandomEffectReducer("random-effect-reducer", 0.5),
    KeepSpaces("keep-spaces", true),
    ReplaceWith("replace-with", ".."),
    NoisePerBlock("noise-per-block", 0.04),
    ShoutRadiusModifier("shout-radius-modifier", 5D),
    WhisperRadiusModifier("whisper-radius-modifier", -5D),
    AddGlobalChat("add-global-chat", true);

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

    public double getDouble() {
        return ChatMuffler.config.getDouble(getNode(), (double) getDefaultValue());
    }

    public String getString() {
        return ChatMuffler.config.getString(getNode(), (String) getDefaultValue());
    }

    public int getInt() {
        return ChatMuffler.config.getInt(getNode(), (int) getDefaultValue());
    }

    public boolean getBoolean() {
        return ChatMuffler.config.getBoolean(getNode(), (boolean) getDefaultValue());
    }

}
