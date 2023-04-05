package pl.bartixen.bxauth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Data.Logs;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class MultiCommand implements CommandExecutor {

    Main plugin;

    DataPlayerManager dataplayer;
    DataManager data;
    static DataMessages messages;

    public MultiCommand(Main m) {
        plugin = m;
        m.getCommand("multi").setExecutor(this);
        m.getCommand("multikonta").setExecutor(this);
        m.getCommand("multiaccounts").setExecutor(this);
        data = DataManager.getInstance();
        messages = DataMessages.getInstance();
        dataplayer = DataPlayerManager.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("bxauth.commands.multi") || sender.isOp()) {
            if (args.length == 1) {
                if ((data.getData().getString(args[0] + ".uuid")) != null) {
                    String uuid = data.getData().getString(args[0] + ".uuid");
                    if (((dataplayer.getData(UUID.fromString(uuid)).getString("first_ip").equals((dataplayer.getData(UUID.fromString(uuid)).getString("last_ip")))))) {
                        String ip = (dataplayer.getData(UUID.fromString(uuid)).getString("first_ip")).replace("/", "").replace(".", "");
                        if (data.getData().getBoolean("useripregister." + ip + ".multikonta")) {
                            data.getData().set("useripregister." + ip + ".multikonta", false);
                            try {
                                data.saveData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.cannot_create_multiple_accounts")).replace("&", "§").replace("{player}", args[0]));
                            if (plugin.getConfig().getBoolean("logs")) {
                                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.cannot_create_multiple_accounts")).replace("&", "§").replace("{sender}", sender.getName()).replace("{player}", args[0]), true);
                            }
                        } else {
                            data.getData().set("useripregister." + ip + ".multikonta", true);
                            try {
                                data.saveData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.can_create_multiple_accounts")).replace("&", "§").replace("{player}", args[0]));
                            if (plugin.getConfig().getBoolean("logs")) {
                                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.can_create_multiple_accounts")).replace("&", "§").replace("{sender}", sender.getName()).replace("{player}", args[0]), true);
                            }
                        }
                    } else {
                        sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.the_player_has_a_variable_IP_address")).replace("&", "§").replace("{player}", args[0]));
                    }
                } else {
                    sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.no_player_in_base")).replace("&", "§").replace("{player}", args[0]));
                }
            } else {
                sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.correct_use_of_multi")).replace("&", "§"));
            }
        } else {
            sender.sendMessage(Objects.requireNonNull(messages.getData().getString("no_permissions")).replace("&", "§").replace("{permission}", "bxauth.commands.multi"));
        }
        return false;
    }
}
