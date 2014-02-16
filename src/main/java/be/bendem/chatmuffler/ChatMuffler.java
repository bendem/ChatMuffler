package be.bendem.chatmuffler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Created by Ben on 14/02/14.
 */
public class ChatMuffler extends JavaPlugin {

    public static Logger                logger;
    public static FileConfiguration     config;
    public        PluginDescriptionFile pdfFile;

    // TODO [ADD] Add static fields for the volume (shout, normal, whisp)
    // where 'normal' is loaded from config, shout is normal + a percentage of itself and same for whisp

    @Override
    public void onEnable() {
        logger = getLogger();
        pdfFile = getDescription();
        config = getConfig();

        saveDefaultConfig();
        // TODO [ADD] Add commands to set range and volume?
        logger.fine(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventHandler(this), this);
    }

    @Override
    public void onDisable() {
        logger.fine(pdfFile.getName() + " want you to have a nice day ;-)");
    }

}
