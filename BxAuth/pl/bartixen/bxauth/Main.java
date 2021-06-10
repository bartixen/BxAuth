package pl.bartixen.bxauth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Commands.*;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Listeners.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Main main;

    DataManager data;

    public String version = getDescription().getVersion();

    public static Map<UUID, Boolean> LoggedIn = new HashMap<UUID, Boolean>();

    public Main() {
        data = DataManager.getInstance();
    }

    @Override
    public void onEnable() {
        if ((!getDescription().getName().contains("BxAuth")) || (!getDescription().getAuthors().contains("Bartixen"))) {
            getLogger().log(Level.WARNING, "§8[========== §9BxAuth §8==========]");
            getLogger().log(Level.WARNING, "§cPlugin zostal wylaczony z powodu edytowania pliku §eplugin.yml");
            getLogger().log(Level.WARNING, "§8[========== §9BxAuth §8==========]");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().log(Level.INFO, "§8[========== §9BxAuth §8==========]");
            getLogger().log(Level.INFO, "§fVersion: §b{0}", getDescription().getVersion());
            getLogger().log(Level.INFO, "§fAuthor: §bBartixen");
            getLogger().log(Level.INFO, "§fWebsite: §bhttps://bartixen.pl");
            getLogger().log(Level.INFO, "§8[========== §9BxAuth §8==========]");

            getConfig().options().copyDefaults(true);
            saveConfig();

            main = this;

            try {
                data.setup(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            data.getData().set("sessions", null);
            try {
                data.saveData();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                UUID uuid = p.getUniqueId();

                p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n§7Zaloguj się ponownie.\n");
                double x = p.getLocation().getX();
                double y = p.getLocation().getY();
                double z = p.getLocation().getZ();
                float yaw = p.getLocation().getYaw();
                float pitch = p.getLocation().getPitch();
                String world = p.getWorld().getName();
                data.getData().set(uuid + ".lastlocation.world", world);
                data.getData().set(uuid + ".lastlocation.x", x);
                data.getData().set(uuid + ".lastlocation.y", y);
                data.getData().set(uuid + ".lastlocation.z", z);
                data.getData().set(uuid + ".lastlocation.yaw", yaw);
                data.getData().set(uuid + ".lastlocation.pitch", pitch);
                data.getData().set(uuid + ".lastplay", format.format(now));
                try {
                    data.saveData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            new LoginCommand(this);
            new RegisterCommand(this);
            new ChangepasswordCommand(this);
            new LogutCommand(this);
            new AutoLogin(this);
            new UnRegisterCommand(this);
            new AutoMessage(this).runTaskTimer(this, 0, 20 * 3);
            new LoginTime(this).runTaskTimer(this, 0, 20);
            new MultiCommand(this);
            new AuthCommand(this);

            try {
                LogFilter.register();
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            }

            getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
            getServer().getPluginManager().registerEvents(new AutoLogin(this), this);
            getServer().getPluginManager().registerEvents(new CheckAccount(this), this);
        }
    }
}
