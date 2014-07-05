package be.bendem.bukkit.chatmuffler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        MessageDispatcher dispatcher = new MessageDispatcher(event.getMessage(), event.getPlayer(), event.getRecipients());
        dispatcher.dispatch();
        event.setCancelled(true);

        // Replicate vanilla behavior
        ChatMuffler.logger.info("[to " + dispatcher.getTargetCount() + " player(s)] <" + event.getPlayer().getDisplayName() + "> " + event.getMessage());
        ChatMuffler.logger.fine("_____________________________");
    }

}
