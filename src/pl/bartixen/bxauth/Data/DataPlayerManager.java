package pl.bartixen.bxauth.Data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import pl.bartixen.bxauth.Main;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class DataPlayerManager {

    Plugin p;

    static DataPlayerManager instance;
    FileConfiguration dataplayer;
    public static File file;

    static {
        instance = new DataPlayerManager();
    }

    public static DataPlayerManager getInstance() {
        return instance;
    }

    public FileConfiguration getData(UUID uuid) {
        if (!Main.getPlugin(Main.class).getDataFolder().exists()) {
            Main.getPlugin(Main.class).getDataFolder().mkdirs();
        }
        File path = new File(Main.getPlugin(Main.class).getDataFolder() + File.separator + "/data_player");
        file = new File(path, String.valueOf(File.separator + uuid + ".yml"));
        if (!file.exists()) {
            try {
                path.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                    if (Main.language.equals("pl")) {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udało się utworzyć pliku §data.yml");
                        Logs.logDebug("Nie udało się utworzyć pliku data.yml", false);
                    } else {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "§cFailed to create the file §data.yml");
                        Logs.logDebug("Failed to create the file data.yml", false);
                    }
                }
            }
        }
        return dataplayer = YamlConfiguration.loadConfiguration(file);
    }

    public void saveData(UUID uuid) throws IOException {
        File path = new File(Main.getPlugin(Main.class).getDataFolder() + File.separator + "/data_player");
        file = new File(path, String.valueOf(File.separator + uuid + ".yml"));
        try {
            dataplayer.save(file);
        } catch (IIOException e) {
            if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                if (Main.language.equals("pl")) {
                    Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udało się zapisać pliku §edata.yml");
                    Logs.logDebug("Nie udało się zapisać pliku data.yml", false);
                } else {
                    Bukkit.getServer().getLogger().log(Level.WARNING, "§cFailed to save the file §edata.yml");
                    Logs.logDebug("Failed to save the file data.yml", false);
                }
            }
        }
    }

    public void reloadData(UUID uuid) {
        File path = new File(Main.getPlugin(Main.class).getDataFolder() + File.separator + "/data_player");
        file = new File(path, String.valueOf(File.separator + uuid + ".yml"));
        dataplayer = YamlConfiguration.loadConfiguration(file);
    }
}
