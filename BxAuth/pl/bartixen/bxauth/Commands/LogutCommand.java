package pl.bartixen.bxauth.Commands;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Main;

import java.util.UUID;
import java.util.logging.Level;

public class LogutCommand implements CommandExecutor {

    Main plugin;

    static DataManager data;

    public LogutCommand(Main m) {
        plugin = m;
        m.getCommand("logut").setExecutor(this);
        m.getCommand("wyloguj").setExecutor(this);
        data = DataManager.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cTa komenda jest przeznaczona tylko dla graczy");
            return false;
        }

        if (plugin.LoggedIn.get(uuid)) {
            if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) != PremiumStatus.PREMIUM) {
                p.sendMessage("§7Zostales wylogowany");
                data.getData().set(uuid + ".notifications", true);
                plugin.LoggedIn.put(uuid, false);
                plugin.getLogger().log(Level.INFO, "Gracz " + p.getDisplayName() + " pomyślnie się wylogował");
            } else {
                p.sendMessage("§7Gracz premium nie może się wylogować");
            }
        } else {
            p.sendMessage("§7Nie jesteś zalogowany");
        }

        return false;
    }
}
