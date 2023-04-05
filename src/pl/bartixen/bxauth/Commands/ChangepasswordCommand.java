package pl.bartixen.bxauth.Commands;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Data.HashPassword;
import pl.bartixen.bxauth.Data.Logs;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class ChangepasswordCommand implements CommandExecutor {

    Main plugin;

    DataPlayerManager dataplayer;

    static DataMessages messages;

    public ChangepasswordCommand(Main m) {
        plugin = m;
        m.getCommand("changepassword").setExecutor(this);
        m.getCommand("changepass").setExecutor(this);
        m.getCommand("zmienhaslo").setExecutor(this);
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
            if (JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
                if (dataplayer.getData(uuid).getBoolean("register")) {
                    if (args.length == 2) {
                        String haslo1 = dataplayer.getData(uuid).getString("password");
                        String ghaslo = HashPassword.tohash(args[0]);
                        String haslo2 = HashPassword.tohash(args[1]);
                        if (haslo1.equals(ghaslo)) {
                            Date now = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss ss:mm:HH dd-MM-yyyy");
                            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.successfully_change_the_password")).replace("&", "§"));
                            dataplayer.getData(uuid).set("password", haslo2);
                            try {
                                dataplayer.saveData(uuid);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dataplayer.getData(uuid).set("changepassword.data", format.format(now));
                            try {
                                dataplayer.saveData(uuid);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (plugin.getConfig().getBoolean("logs")) {
                                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.changepassword")).replace("&", "§").replace("{player}", p.getName()), true);
                            }
                        } else {
                            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.the_password_is_incorrect")).replace("&", "§"));
                        }
                    } else {
                        p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.correct_use_of_changepassword")).replace("&", "§"));
                    }
                } else {
                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.use_register")).replace("&", "§"));
                }
            } else {
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.premium_player_cannot_changepassword")).replace("&", "§"));
            }
        } else {
            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.not_logged_in")).replace("&", "§"));
        }
        return false;
    }
}
