package pl.bartixen.bxauth.Commands;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Data.Logs;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class UnRegisterCommand implements CommandExecutor {

    Main plugin;

    DataPlayerManager dataplayer;
    DataManager data;
    static DataMessages messages;

    public UnRegisterCommand(Main m) {
        plugin = m;
        m.getCommand("unregister").setExecutor(this);
        m.getCommand("unreg").setExecutor(this);
        m.getCommand("odrejestruj").setExecutor(this);
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Objects.requireNonNull(messages.getData().getString("command_only_for_players")).replace("&", "§"));
                return false;
            }
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();
            if (plugin.LoggedIn.get(uuid)) {
                if (JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
                    if (dataplayer.getData(uuid).getBoolean("register")) {
                        dataplayer.getData(uuid).set("register", false);
                        try {
                            dataplayer.saveData(uuid);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        data.getData().set("sessions." + p.getName(), null);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dataplayer.getData(uuid).set("password", null);
                        try {
                            dataplayer.saveData(uuid);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dataplayer.getData(uuid).set("lastlocation", null);
                        try {
                            dataplayer.saveData(uuid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.unregister")).replace("&", "§") + "\n");
                        if (plugin.getConfig().getBoolean("logs")) {
                            Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.unregister")).replace("&", "§").replace("{player}", p.getName()), true);
                        }
                    } else {
                        p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.use_register")).replace("&", "§"));
                    }
                } else {
                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.premium_player_cannot_unregister")).replace("&", "§"));
                }
            } else {
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.use_login")).replace("&", "§"));
            }
        } else {
            if (args.length == 1) {
                if (sender.hasPermission("bxauth.commands.unregister") || sender.isOp()) {
                    String uuid = data.getData().getString(args[0] + ".uuid");
                    if (dataplayer.getData(UUID.fromString(uuid)).getBoolean("register")) {
                        dataplayer.getData(UUID.fromString(uuid)).set("register", false);
                        try {
                            dataplayer.saveData(UUID.fromString(uuid));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        data.getData().set("sessions." + args[0], null);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dataplayer.getData(UUID.fromString(uuid)).set("password", null);
                        try {
                            dataplayer.saveData(UUID.fromString(uuid));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dataplayer.getData(UUID.fromString(uuid)).set("lastlocation", null);
                        try {
                            dataplayer.saveData(UUID.fromString(uuid));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dataplayer.getData(UUID.fromString(uuid)).set("premium", null);
                        try {
                            dataplayer.saveData(UUID.fromString(uuid));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dataplayer.getData(UUID.fromString(uuid)).set("check_account", null);
                        try {
                            dataplayer.saveData(UUID.fromString(uuid));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.unregister_player")).replace("&", "§").replace("{player}", args[0]));
                        if (plugin.getConfig().getBoolean("logs")) {
                            Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.deregistering")).replace("&", "§").replace("{player}", args[0]).replace("{sender}", sender.getName()), true);
                        }
                    } else {
                        sender.sendMessage(Objects.requireNonNull(messages.getData().getString("player.the_player_is_not_registered")).replace("&", "§").replace("{player}", args[0]));
                    }
                } else {
                    sender.sendMessage(Objects.requireNonNull(messages.getData().getString("no_permissions")).replace("&", "§").replace("{permission}", "bxauth.commands.unregister"));
                }
            }
        }
        return false;
    }
}