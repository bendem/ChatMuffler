package be.bendem.chatmuffler;

import java.util.Random;

/**
 * Created by Ben on 15/02/14.
 */
public class NoiseGenerator {

    private final String  originalMessage;
    private final double  noise;
    private final double  randomEffectReducer;
    private final boolean keepSpaces;
    private final String  replaceWith;
    private int nbKeptChars = 0;

    public NoiseGenerator(double noise, String message) {
        this(noise, message, 0D, true, "..");
    }

    public NoiseGenerator(double noise, String originalMessage, double randomEffectReducer, boolean keepSpaces, String replaceWith) {
        this.noise = noise;
        this.originalMessage = originalMessage;
        this.randomEffectReducer = randomEffectReducer;
        this.keepSpaces = keepSpaces;
        this.replaceWith = replaceWith;
    }

    public String getNoisifiedMessage() {
        nbKeptChars = getOriginalMessage().length();
        StringBuilder messageBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = getOriginalMessage().length() - 1; i >= 0; --i) {
            if(random.nextDouble() * randomEffectReducer + getNoise() > 1) {
                if(getOriginalMessage().charAt(i) == ' ' && keepSpaces) {
                    messageBuilder.insert(0, ' ');
                } else {
                    messageBuilder.insert(0, replaceWith);
                }
                --nbKeptChars;
            } else {
                messageBuilder.insert(0, getOriginalMessage().charAt(i));
            }
        }

        return messageBuilder.toString();
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public double getNoise() {
        return noise;
    }

    public double getRandomEffectReducer() {
        return randomEffectReducer;
    }

    public boolean isKeepSpaces() {
        return keepSpaces;
    }

    public String getReplaceWith() {
        return replaceWith;
    }

    public int getNbKeptChars() {
        return nbKeptChars;
    }

}
