package pl.bartixen.bxauth.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Main;

public class AuthCommand implements CommandExecutor {

    Main plugin;

    DataManager data;

    public AuthCommand(Main m) {
        plugin = m;
        m.getCommand("auth").setExecutor(this);
        m.getCommand("bxauth").setExecutor(this);
        data = DataManager.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String version = plugin.version;
        if (args.length == 1) {
            if (sender.hasPermission("bxauth.commands.reload") || sender.isOp()) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage("§8 • — • — • — • ");
                    plugin.reloadConfig();
                    sender.sendMessage("§7Pomyślnie przeładowano §9CONFIG");
                    data.reloadData();
                    sender.sendMessage("§7Pomyślnie przeładowano §9DATAMENAGER");
                    sender.sendMessage("§8 • — • — • — • ");
                } else {
                    sendmsgpl(sender, version);
                }
            } else {
                sendmsgpl(sender, version);
            }
        } else {
            sendmsgpl(sender, version);
        }
        return false;
    }

    public void sendmsgpl(CommandSender sender, String version) {
        sender.sendMessage("§7");
        sender.sendMessage("§7Plugin §eBxAuth");
        sender.sendMessage("§7Version: §e" + version);
        sender.sendMessage("§7Author: §eBartixen");
        sender.sendMessage("§7Website: §ehttps://bartixen.pl");
        sender.sendMessage("§7");
    }

}