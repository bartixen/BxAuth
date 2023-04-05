package pl.bartixen.bxauth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Data.Logs;
import pl.bartixen.bxauth.Main;

import java.util.Objects;
import java.util.UUID;

public class LogutCommand implements CommandExecutor {

    static Main plugin;

    DataPlayerManager dataplayer;
    static DataMessages messages;

    public LogutCommand(Main m) {
        plugin = m;
        m.getCommand("logut").setExecutor(this);
        m.getCommand("wyloguj").setExecutor(this);
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();
        if (!(sender instanceof Player)) {
            sender.sendMessage(Objects.requireNonNull(messages.getData().getString("command_only_for_players")).replace("&", "§"));
            return false;
        }
        if (plugin.LoggedIn.get(uuid)) {
            p.kickPlayer(Objects.requireNonNull(messages.getData().getString("player.logging_out")).replace("&", "§"));
            if (plugin.getConfig().getBoolean("logs")) {
                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.logging_out")).replace("&", "§").replace("{player}", p.getName()), true);
            }
        } else {
            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.not_logged_in")).replace("&", "§"));
        }
        return false;
    }

}
