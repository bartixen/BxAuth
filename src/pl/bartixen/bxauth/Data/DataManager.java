package pl.bartixen.bxauth.Data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.bartixen.bxauth.Main;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {

    Plugin p;

    static DataManager instance;
    FileConfiguration data;
    public static File file;

    static {
        instance = new DataManager();
    }

    public static DataManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) throws IOException {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdirs();
        }
        File path = new File(p.getDataFolder() + File.separator + "/data");
        file = new File(path, String.valueOf(File.separator + "data.yml"));
        if (!file.exists()) {
            try {
                path.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                    if (Main.language.equals("pl")) {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "Nie udalo sie utworzyc pliku §edata.yml");
                        Logs.logDebug("Nie udalo sie utworzyc pliku messages_pl.yml", false);
                    } else {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to create the file §edata.yml");
                        Logs.logDebug("Failed to create the file messages_pl.yml", false);
                    }
                }
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveData() throws IOException {
        try {
            data.save(file);
        } catch (IIOException e) {
            if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                if (Main.language.equals("pl")) {
                    Bukkit.getServer().getLogger().log(Level.WARNING, "Nie udalo sie zapisac pliku data.yml");
                    Logs.logDebug("Nie udalo sie zapisac pliku messages_pl.yml", false);
                } else {
                    Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to save the file data.yml");
                    Logs.logDebug("Failed to save the file messages_pl.yml", false);
                }
            }
        }
    }

    public void reloadData() {
        if (Main.language.equals("pl")) {
            data = YamlConfiguration.loadConfiguration(file);
        } else {
            data = YamlConfiguration.loadConfiguration(file);
        }
    }
}
