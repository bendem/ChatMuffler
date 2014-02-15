package be.bendem.chatmuffler;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Ben on 14/02/14.
 */
public class AsyncPlayerChatEventHandler implements Listener {

    ChatMuffler plugin;

    public AsyncPlayerChatEventHandler(ChatMuffler plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        // TODO [ADD] Change this to the distance between players (loop trough connected players)
        // TODO [ADD] Add a global / shout / whisp chat using a special chars (like @) or a commands (like /g, /shout, /whisp)
        // TODO [ADD] Add permissions (chatmuffler.*, chatmuffler.global.send, chatmuffler.global.receive, chatmuffler.shout, chatmuffler.whisper)
        // TODO [FIX] Split the logic of this method
        Location playerLocation = event.getPlayer().getLocation();
        Location spawnLocation = event.getPlayer().getWorld().getSpawnLocation();
        String originalMessage = event.getMessage();

        plugin.logger.info("Distance to spawn : " + distanceBetween(playerLocation, spawnLocation));

        double distance = distanceBetween(playerLocation, spawnLocation) - plugin.getConfig().getInt("radius", 20);
        if(distance <= 0) {
            return;
        }

        double noise = distance * plugin.getConfig().getDouble("noise-per-block", 0.05);
        if(noise > 1) {
            event.setCancelled(true);
            return;
        }

        NoiseGenerator noiseGenerator = new NoiseGenerator(
            noise, originalMessage,
            plugin.getConfig().getDouble("random-effect-reducer", 0.5),
            plugin.getConfig().getBoolean("keep-spaces", true),
            plugin.getConfig().getString("replace-with", "..")
        );

        String noisifiedMessage = noiseGenerator.getNoisifiedMessage();
        int nbKeptChars = noiseGenerator.getNbKeptChars();

        plugin.logger.info("nb kept " + nbKeptChars + " on " + originalMessage.length() + " (" + (float) nbKeptChars / originalMessage.length() + "%)");
        if(nbKeptChars == 0 || (float) nbKeptChars / originalMessage.length() < plugin.getConfig().getDouble(Config.RemainingCharsNeeded.getNode(), 0.3)) {
            event.setCancelled(true);
            return;
        }
        event.setMessage(noisifiedMessage);
        plugin.logger.info("Char restants : " + nbKeptChars + ", noise : " + noise);
    }

    public double distanceBetween(Location l1, Location l2) {
        return l1.distance(l2);
    }

    public MessageType getMessageType(String message) {
        if(message == null) {
            return null;
        }
        String firstChar = message.substring(0, 1);
        for(MessageType type : MessageType.values()) {
            if(firstChar.equals(plugin.getConfig().getString(type.getConfigNode(), type.getDefaultValue()))
                    && !type.equals(MessageType.Normal)) {
                return type;
            }
        }
        return MessageType.Normal;
    }

}
