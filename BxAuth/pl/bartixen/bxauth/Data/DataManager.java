package pl.bartixen.bxauth.Data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {

    static DataManager instance;
    Plugin p;
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
        File path = new File(p.getDataFolder() + File.separator);
        file = new File(path, String.valueOf(File.separator + "data.yml"));
        if (!file.exists()) {
            try {
                path.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().log(Level.WARNING, "Nie udalo sie utworzyc pliku §edata.yml");
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
            Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udalo sie zapisac pliku §edata.yml");
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(file);
    }
}
