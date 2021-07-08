package pl.bartixen.bxauth.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.HashPassword;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class LoginCommand implements CommandExecutor {

    Main plugin;

    static DataManager data;

    public LoginCommand(Main m) {
        plugin = m;
        m.getCommand("login").setExecutor(this);
        m.getCommand("zaloguj").setExecutor(this);
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

        if (!plugin.LoggedIn.get(uuid)) {
            if (data.getData().getBoolean(uuid + ".register")) {
                if (args.length == 1) {
                    String haslo1 = data.getData().getString(uuid + ".password");
                    String haslo2 = HashPassword.tohash(args[0]);
                    if (haslo1.equals(haslo2)) {
                        p.sendMessage("§7Zalogowano pomyslnie");
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        InetSocketAddress IPAdressPlayer = p.getAddress();
                        String ip = IPAdressPlayer.toString();
                        int entityTypeLenght = ip.length() - 6;
                        String ipgracza = ip.substring(0, entityTypeLenght);
                        data.getData().set("sessions." + p.getName() + ".date", format.format(now));
                        data.getData().set("sessions." + p.getName() + ".ip", ipgracza);
                        data.getData().set(uuid + ".lastlogin", format.format(now));
                        data.getData().set(uuid + ".notifications", false);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        plugin.LoggedIn.put(uuid, true);
                        if (plugin.getConfig().getBoolean("safelocation")) {
                            double x = data.getData().getDouble(uuid + ".lastlocation.x");
                            double y = data.getData().getDouble(uuid + ".lastlocation.y");
                            double z = data.getData().getDouble(uuid + ".lastlocation.z");
                            float yaw = data.getData().getInt(uuid + ".lastlocation.yaw");
                            float pitch = data.getData().getInt(uuid + ".lastlocation.pitch");
                            String world = data.getData().getString(uuid + ".lastlocation.world");
                            p.teleport(new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch));
                        }
                        plugin.getLogger().log(Level.INFO, "Gracz " + p.getDisplayName() + " pomyślnie zalogował sie przez hasło");
                    } else {
                        p.sendMessage("§7Podane hasło jest niepoprawne");
                    }
                } else {
                    p.sendMessage("§7Poprawne użycie: §9/login [hasło]");
                }
            } else {
                p.sendMessage("§7Najpierw użyj: §9/register");
            }
        } else {
            p.sendMessage("§7Jesteś już zalogowany");
        }

        return false;
    }
}
