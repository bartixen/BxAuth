package pl.bartixen.bxauth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.util.logging.Level;

public class MultiCommand implements CommandExecutor {

    Main plugin;

    static DataManager data;

    public MultiCommand(Main m) {
        plugin = m;
        m.getCommand("multi").setExecutor(this);
        m.getCommand("multikonta").setExecutor(this);
        data = DataManager.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("bxauth.commands.multi") || sender.isOp()) {
            if (args.length == 1) {
                if ((data.getData().getString(args[0] + ".uuid")) != null) {
                    String uuid = data.getData().getString(args[0] + ".uuid");
                    if (((data.getData().getString(uuid + ".first_ip").equals((data.getData().getString(uuid + ".last_ip")))))) {
                        String ip = (data.getData().getString(uuid + ".first_ip")).replace("/", "").replace(".", "");
                        if (data.getData().getBoolean("useripregister." + ip + ".multikonta")) {
                            data.getData().set("useripregister." + ip + ".multikonta", false);
                            try {
                                data.saveData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sender.sendMessage("§7Od teraz gracz §9" + args[0] + " §7nie moze tworzyc multikont");
                            plugin.getLogger().log(Level.INFO, "Gracz " + sender.getName() + " pomyslnie zabral mozliwosc tworzenia kont dla " + args[0]);
                        } else {
                            data.getData().set("useripregister." + ip + ".multikonta", true);
                            try {
                                data.saveData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sender.sendMessage("§7Od teraz gracz §9" + args[0] + " §7moze tworzyc multikonta");
                            plugin.getLogger().log(Level.INFO, "Gracz " + sender.getName() + " pomyslnie nadal mozliwosc tworzenia kont dla " + args[0]);
                        }
                    } else {
                        sender.sendMessage("§7Gracz §9" + args[0] + " §7ma zmienne IP");
                    }
                } else {
                    sender.sendMessage("§7Nie znaleziono gracza §9" + args[0] + " §7w bazie danych");
                }
            } else {
                sender.sendMessage("§7Poprawne uzycie: §9/multikonta [gracz]");
            }
        } else {
            sender.sendMessage("§7Brak permisji: §9bxauth.commands.multi");
        }

        return false;
    }
}
