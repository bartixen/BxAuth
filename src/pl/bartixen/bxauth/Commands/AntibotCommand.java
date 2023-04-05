package pl.bartixen.bxauth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Data.Logs;
import pl.bartixen.bxauth.Main;

import java.util.*;

public class AntibotCommand implements CommandExecutor {

    Main plugin;

    static DataMessages messages;

    public AntibotCommand(Main m) {
        plugin = m;
        m.getCommand("antibot").setExecutor(this);
        m.getCommand("antybot").setExecutor(this);
        messages = DataMessages.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("bxauth.commands.antibot") || sender.isOp()) {
            if (plugin.getConfig().getBoolean("antibot")) {
                plugin.getConfig().set("antibot", false);
                sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.antibot_disable")).replace("&", "§"));
                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.antibot_disable")).replace("&", "§").replace("{player}", sender.getName()), true);
            } else {
                plugin.getConfig().set("antibot", true);
                sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.antibot_enable")).replace("&", "§"));
                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.antibot_enable")).replace("&", "§").replace("{player}", sender.getName()), true);
            }
        } else {
            sender.sendMessage(Objects.requireNonNull(messages.getData().getString("no_permissions")).replace("&", "§").replace("{permission}", "bxauth.commands.antibot"));
        }
        return false;
    }
}