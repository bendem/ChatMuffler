package be.bendem.bukkit.chatmuffler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ben on 14/02/14.
 */
public class ChatMuffler extends JavaPlugin implements CommandExecutor {

    public static Logger                logger;
    public static FileConfiguration     config;
    public        PluginDescriptionFile pdfFile;

    // TODO [ADD] Add static fields for the volume (shout, normal, whisp)
    // where 'normal' is loaded from config, shout is normal + a percentage of itself and same for whisp

    @Override
    public void onEnable() {
        logger = getLogger();
        logger.setLevel(Level.ALL);
        pdfFile = getDescription();
        config = getConfig();

        saveDefaultConfig();
        getCommand("chatmuffler").setExecutor(this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatEventHandler(this), this);
        logger.fine(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    @Override
    public void onDisable() {
        logger.fine(pdfFile.getName() + " want you to have a nice day ;-)");
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender.hasPermission("chatmuffler.reload")) {
            if(args.length == 1 && args[0].equals("reload")) {
                reloadConfig();
                commandSender.sendMessage(ChatColor.GRAY + "Config reloaded...");
                return true;
            }
            return false;
        }
        commandSender.sendMessage(ChatColor.RED + "You don't have access to this command");
        return true;
    }


}
