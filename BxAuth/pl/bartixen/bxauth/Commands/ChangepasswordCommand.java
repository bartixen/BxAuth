package pl.bartixen.bxauth.Commands;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.HashPassword;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class ChangepasswordCommand implements CommandExecutor {

    Main plugin;

    static DataManager data;

    public ChangepasswordCommand(Main m) {
        plugin = m;
        m.getCommand("changepassword").setExecutor(this);
        m.getCommand("changepass").setExecutor(this);
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
                if (data.getData().getBoolean(uuid + ".register")) {
                    if (args.length == 2) {
                        String haslo1 = data.getData().getString(uuid + ".password");
                        String ghaslo = HashPassword.tohash(args[0]);
                        String haslo2 = HashPassword.tohash(args[1]);
                        if (haslo1.equals(ghaslo)) {
                            Date now = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss ss:mm:HH dd-MM-yyyy");
                            p.sendMessage("§7Pomyslnie zmieniono haslo");
                            data.getData().set(uuid + ".password", haslo2);
                            data.getData().set(uuid + ".changepassword.data", format.format(now));
                            try {
                                data.saveData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            plugin.getLogger().log(Level.INFO, "Gracz " + p.getDisplayName() + " pomyslnie zmienil haslo");
                        } else {
                            p.sendMessage("§7Podane haslo jest niepoprawne");
                        }
                    } else {
                        p.sendMessage("§7Poprawne uzycie: §9/changepassword [stare_haslo] [nowe_haslo]");
                    }
                } else {
                    p.sendMessage("§7Najpierw uzyj: §9/register");
                }
            } else {
                p.sendMessage("§7Gracz premium nie moze zmienic hasla");
            }
        } else {
            p.sendMessage("§7Najpierw uzyj: §9/login");
        }

        return false;
    }
}
