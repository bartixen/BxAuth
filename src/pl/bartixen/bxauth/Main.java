package pl.bartixen.bxauth;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Commands.*;
import pl.bartixen.bxauth.Data.*;
import pl.bartixen.bxauth.Listeners.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Main extends JavaPlugin {

    public static Main main;

    DataPlayerManager dataplayer;
    DataMessages messages;
    DataManager data;

    public CheckFile CheckFile = new CheckFile(this);
    public String version = getDescription().getVersion();
    public static Map<UUID, Boolean> LoggedIn = new HashMap<UUID, Boolean>();
    public static String language;

    public Main() {
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @Override
    public void onEnable() {
        language = getConfig().getString("messages");
        if ((!getDescription().getName().contains("BxAuth")) || (!getDescription().getAuthors().contains("Bartixen"))) {
            getLogger().log(Level.WARNING, "[========== BxAuth ==========]");
            getLogger().log(Level.WARNING, "The plugin has been disabled due to file editing plugin.yml");
            getLogger().log(Level.WARNING, "[========== BxAuth ==========]");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            if (getConfig().getBoolean("logs")) {
                if (Main.language.equals("pl")) {
                    Logs.logDebug("Wtyczka zostala wylaczona z powodu edycji pliku plugin.yml", false);
                } else {
                    Logs.logDebug("The plugin has been disabled due to file editing plugin.yml", false);
                }
            }
        } else {
            getLogger().log(Level.INFO, "[========== BxAuth ==========]");
            getLogger().log(Level.INFO, "Version: {0}", getDescription().getVersion());
            getLogger().log(Level.INFO, "Author: Bartixen");
            getLogger().log(Level.INFO, "Website: https://bartixen.pl");
            getLogger().log(Level.INFO, "[========== BxAuth ==========]");
            if (getConfig().getBoolean("logs")) {
                if (Main.language.equals("pl")) {
                    Logs.logDebug("Wtyczka zostala uruchomiona pomyslnie", false);
                } else {
                    Logs.logDebug("The plugin has run successfully", false);
                }
            }

            getConfig().options().copyDefaults(true);
            saveConfig();

            main = this;

            try {
                messages.setup(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

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

            try {
                CheckFile.check(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                UUID uuid = p.getUniqueId();

                p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("reload")).replace("&", "§") + "\n");
                double x = p.getLocation().getX();
                double y = p.getLocation().getY();
                double z = p.getLocation().getZ();
                float yaw = p.getLocation().getYaw();
                float pitch = p.getLocation().getPitch();
                String world = p.getWorld().getName();
                dataplayer.getData(uuid).set("lastlocation.world", world);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataplayer.getData(uuid).set("lastlocation.x", x);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataplayer.getData(uuid).set("lastlocation.y", y);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataplayer.getData(uuid).set("lastlocation.z", z);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataplayer.getData(uuid).set("lastlocation.yaw", yaw);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataplayer.getData(uuid).set("lastlocation.pitch", pitch);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dataplayer.getData(uuid).set("lastplay", format.format(now));
                try {
                    dataplayer.saveData(uuid);
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
            new AntibotCommand(this);

            getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
            getServer().getPluginManager().registerEvents(new AutoLogin(this), this);

            if (getServer().getPluginManager().isPluginEnabled("FastLogin")) {
                new FastLoginHook().register();
            }

        }
    }

    @Override
    public void onDisable(){
        getLogger().log(Level.INFO, "[========== BxAuth ==========]");
        getLogger().log(Level.INFO, "The plug has been disabled");
        getLogger().log(Level.INFO, "[========== BxAuth ==========]");
        if (getConfig().getBoolean("logs")) {
            if (Main.language.equals("pl")) {
                Logs.logDebug("Wtyczka zostala wylaczona", false);
            } else {
                Logs.logDebug("The plug has been disabled", false);
            }
        }
    }
}
