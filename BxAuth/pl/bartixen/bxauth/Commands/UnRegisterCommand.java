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

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class UnRegisterCommand implements CommandExecutor {

    Main plugin;

    static DataManager data;

    public UnRegisterCommand(Main m) {
        plugin = m;
        m.getCommand("unregister").setExecutor(this);
        m.getCommand("unreg").setExecutor(this);
        m.getCommand("odrejestruj").setExecutor(this);
        data = DataManager.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cTa komenda jest przeznaczona tylko dla graczy");
                return false;
            }
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();
            if (plugin.LoggedIn.get(uuid)) {
                if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) != PremiumStatus.PREMIUM) {
                    if (data.getData().getBoolean(uuid + ".register")) {
                        data.getData().set(uuid + ".register", false);
                        data.getData().set("sessions." + p.getName(), null);
                        data.getData().set(uuid + ".password", null);
                        data.getData().set(uuid + ".lastlocation", null);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n§7Zostales pomyslnie wyrejestrowany\n");
                        plugin.getLogger().log(Level.INFO, "Gracz " + p.getName() + " pomyslnie wyrejestrowal sie");
                    } else {
                        p.sendMessage("§7Najpierw uzyj: §9/register");
                    }
                } else {
                    p.sendMessage("§7Gracz premium nie moze sie wyrejestrowac");
                }
            } else {
                p.sendMessage("§7Najpierw uzyj: §9/login");
            }
        } else {
            if (args.length == 1) {
                if (sender.hasPermission("bxauth.commands.unregister") || sender.isOp()) {
                    String uuid = data.getData().getString(args[0] + ".uuid");
                    if (data.getData().getBoolean(uuid + ".register")) {
                        data.getData().set(uuid + ".register", false);
                        data.getData().set("sessions." + args[0], null);
                        data.getData().set(uuid + ".password", null);
                        data.getData().set(uuid + ".lastlocation", null);
                        data.getData().set(uuid + ".premium", null);
                        data.getData().set(uuid + ".check_account", null);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sender.sendMessage("§7Gracz §9" + args[0] + " §7zostal pomyslnie wyrejestrowany");
                        plugin.getLogger().log(Level.INFO, "Gracz " + args[0] + " pomyslnie zostal wyrejestrowany przez " + sender.getName());
                    } else {
                        sender.sendMessage("§7Gracz §9" + args[0] + " §7nie jest zarejestrowany");
                    }
                } else {
                    sender.sendMessage("§7Brak permisji: §9bxauth.commands.unregister");
                }
            }
        }
        return false;
    }
}
