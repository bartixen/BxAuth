package pl.bartixen.bxauth.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class AutoMessage extends BukkitRunnable {

    Main plugin;

    DataPlayerManager dataplayer;
    DataManager data;
    DataMessages messages;

    public AutoMessage(Main m) {
        plugin = m;
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            if ((!plugin.LoggedIn.get(uuid)) && dataplayer.getData(uuid).getBoolean("notifications")) {
                if (!(dataplayer.getData(uuid).getBoolean("register"))) {
                    String captcha = dataplayer.getData(uuid).getString("captcha");
                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("automessage.register")).replace("&", "§").replace("{captcha}", captcha));
                } else {
                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("automessage.login")).replace("&", "§"));
                }
            }
        }
    }
}