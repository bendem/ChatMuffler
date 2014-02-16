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
        MessageDispatcher dispatcher = new MessageDispatcher(event.getMessage(), event.getPlayer());
        dispatcher.dispatch();
        event.setCancelled(true);
    }

}
