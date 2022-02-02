package pl.bartixen.bxauth.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.util.UUID;

public class LoginTime extends BukkitRunnable {

    Main plugin;

    static DataManager data;

    public LoginTime(Main m) {
        plugin = m;
        data = DataManager.getInstance();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            if (!plugin.LoggedIn.get(uuid)) {
                if ((data.getData().getInt(uuid + ".logintime")) > plugin.getConfig().getInt("timeout")) {
                    p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n§7Twój czas na uwierzytelnienie miną.\n");
                } else {
                    data.getData().set(uuid + ".logintime", data.getData().getInt(uuid + ".logintime") + 1);
                    try {
                        data.saveData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
