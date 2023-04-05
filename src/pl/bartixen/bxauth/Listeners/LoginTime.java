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

public class LoginTime extends BukkitRunnable {

    Main plugin;

    DataPlayerManager dataplayer;
    DataManager data;
    DataMessages messages;

    public LoginTime(Main m) {
        plugin = m;
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            if (!plugin.LoggedIn.get(uuid)) {
                if ((dataplayer.getData(uuid).getInt("logintime")) > plugin.getConfig().getInt("timeout")) {
                    p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.login_time")).replace("&", "§") + "\n");
                } else {
                    dataplayer.getData(uuid).set("logintime", dataplayer.getData(uuid).getInt("logintime") + 1);
                    try {
                        dataplayer.saveData(uuid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
