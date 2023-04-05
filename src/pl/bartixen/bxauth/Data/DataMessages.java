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

public class DataMessages {

    Plugin p;

    static DataMessages instance;
    FileConfiguration messages;
    public static File filePL;
    public static File fileEN;

    static {
        instance = new DataMessages();
    }

    public static DataMessages getInstance() {
        return instance;
    }

    public void setup(Plugin p) throws IOException {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdirs();
        }
        File path = new File(p.getDataFolder() + File.separator + "/messages");
        if (Main.language.equals("pl")) {
            filePL = new File(path, String.valueOf(File.separator + "messages_pl.yml"));
            if (!filePL.exists()) {
                try {
                    path.mkdirs();
                    filePL.createNewFile();
                } catch (IOException e) {
                    if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                        if (Main.language.equals("pl")) {
                            Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udało się utworzyć pliku §emessages_pl.yml");
                            Logs.logDebug("Nie udało się utworzyć pliku messages_pl.yml", false);
                        } else {
                            Bukkit.getServer().getLogger().log(Level.WARNING, "§cFailed to create the file §emessages_pl.yml");
                            Logs.logDebug("Failed to create the file messages_pl.yml", false);
                        }
                    }
                }
            }
            messages = YamlConfiguration.loadConfiguration(filePL);
        } else {
            fileEN = new File(path, String.valueOf(File.separator + "messages_en.yml"));
            if (!fileEN.exists()) {
                try {
                    path.mkdirs();
                    fileEN.createNewFile();
                } catch (IOException e) {
                    if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                        if (Main.language.equals("pl")) {
                            Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udało się utworzyć pliku §emessages_en.yml");
                            Logs.logDebug("Nie udało się utworzyć pliku messages_en.yml", false);
                        } else {
                            Bukkit.getServer().getLogger().log(Level.WARNING, "§cFailed to create the file §emessages_en.yml");
                            Logs.logDebug("Failed to create the file messages_en.yml", false);
                        }
                    }
                }
            }
            messages = YamlConfiguration.loadConfiguration(fileEN);
        }
    }

    public FileConfiguration getData() {
        return messages;
    }

    public void saveData() throws IOException {
        if (Main.language.equals("pl")) {
            try {
                messages.save(filePL);
            } catch (IIOException e) {
                if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                    if (Main.language.equals("pl")) {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udało się zapisać pliku §emessages_pl.yml");
                        Logs.logDebug("Nie udało się zapisać pliku messages_pl.yml", false);
                    } else {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "§cFailed to save the file §emessages_pl.yml");
                        Logs.logDebug("Failed to save the file messages_pl.yml", false);
                    }
                }
            }
        } else {
            try {
                messages.save(fileEN);
            } catch (IIOException e) {
                if (Main.getPlugin(Main.class).getConfig().getBoolean("logs")) {
                    if (Main.language.equals("pl")) {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "§cNie udało się zapisać pliku §emessages_en.yml");
                        Logs.logDebug("Nie udało się zapisać pliku messages_en.yml", false);
                    } else {
                        Bukkit.getServer().getLogger().log(Level.WARNING, "§cFailed to save the file §emessages_en.yml");
                        Logs.logDebug("Failed to save the file messages_en.yml", false);
                    }
                }
            }
        }
    }

    public void reloadData() {
        if (Main.language.equals("pl")) {
            messages = YamlConfiguration.loadConfiguration(filePL);
        } else {
            messages = YamlConfiguration.loadConfiguration(fileEN);
        }
    }
}
