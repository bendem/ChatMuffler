package be.bendem.chatmuffler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;

/**
 * Created by Ben on 14/02/14.
 */
public class AsyncPlayerChatEventHandler implements Listener {

    ChatMuffler plugin;

    public AsyncPlayerChatEventHandler(ChatMuffler plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        MessageDispatcher dispatcher = new MessageDispatcher(event.getMessage(), event.getPlayer(), (HashSet) event.getRecipients());
        dispatcher.dispatch();
        ChatMuffler.logger.info("<" + event.getPlayer().getDisplayName() + ">" + event.getMessage()); // Replicate vanilla behavior
        ChatMuffler.logger.info("_____________________________");
        event.setCancelled(true);
    }

}
