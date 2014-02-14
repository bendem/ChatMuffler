package be.bendem.chatmuffler;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

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
        Location playerLocation = event.getPlayer().getLocation();
        // TODO [ADD] Change this to the distance between players (loop trough connected players)
        // TODO [ADD] Add a global / shout / whisp chat using a special chars (like @) or a commands (like /g, /shout, /whisp)
        // TODO [ADD] Add permissions (chatmuffler.*, chatmuffler.global.send, chatmuffler.global.receive, chatmuffler.shout, chatmuffler.whisper)
        // TODO [FIX] Split the logic of this method
        Location spawnLocation = event.getPlayer().getWorld().getSpawnLocation();

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

        int nbKeptChars = event.getMessage().length();
        StringBuilder message = new StringBuilder();
        Random random = new Random();
        for(int i = event.getMessage().length() - 1; i >= 0; --i) {
            if(random.nextDouble() * plugin.getConfig().getDouble("random-effect-reducer", 0.5) + noise > 1) {
                if(event.getMessage().charAt(i) == ' ' && plugin.getConfig().getBoolean("keep-spaces", true)) {
                    message.insert(0, ' ');
                } else {
                    message.insert(0, plugin.getConfig().getString("replace-with", "-"));
                }
                --nbKeptChars;
            } else {
                message.insert(0, event.getMessage().charAt(i));
            }
        }

        plugin.logger.info("nb kept " + nbKeptChars +" on " + event.getMessage().length() + " (" + (float) nbKeptChars / event.getMessage().length() + "%)");
        if(nbKeptChars == 0 || (float) nbKeptChars / event.getMessage().length() < plugin.getConfig().getDouble("remaining-chars-percentage-needed", 0.3)) {
            event.setCancelled(true);
            return;
        }
        event.setMessage(message.toString());
        plugin.logger.info("Char restants : " + nbKeptChars + ", noise : " + noise);
    }

    public double distanceBetween(Location l1, Location l2) {
        return Math.sqrt(
                Math.pow(l2.getX() - l1.getX(), 2) + Math.pow(l2.getY() - l1.getY(), 2) + Math.pow(l2.getZ() - l1.getZ(), 2)
        );
    }

}
