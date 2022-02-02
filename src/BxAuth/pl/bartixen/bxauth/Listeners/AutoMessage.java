package pl.bartixen.bxauth.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Main;

import java.util.UUID;

public class AutoMessage extends BukkitRunnable {

    Main plugin;

    static DataManager data;

    public AutoMessage(Main m) {
        plugin = m;
        data = DataManager.getInstance();
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            if ((!plugin.LoggedIn.get(uuid)) && data.getData().getBoolean(uuid + ".notifications")) {
                if (!(data.getData().getBoolean(uuid + ".register"))) {
                    String captcha = data.getData().getString(uuid + ".captcha");
                    p.sendMessage("§7Zarejestruj się przy użyciu: §9/register [haslo] [haslo] §e" + captcha);
                } else {
                    p.sendMessage("§7Zaloguj się przy użyciu: §9/login [haslo]");
                }
            }
        }
    }
}