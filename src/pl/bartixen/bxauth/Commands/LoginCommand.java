package pl.bartixen.bxauth.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.bartixen.bxauth.Data.*;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class LoginCommand implements CommandExecutor {

    Main plugin;

    DataPlayerManager dataplayer;
    DataManager data;
    static DataMessages messages;

    public LoginCommand(Main m) {
        plugin = m;
        m.getCommand("login").setExecutor(this);
        m.getCommand("zaloguj").setExecutor(this);
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();
        if (!(sender instanceof Player)) {
            sender.sendMessage(Objects.requireNonNull(messages.getData().getString("command_only_for_players")).replace("&", "§"));
            return false;
        }
        if (!plugin.LoggedIn.get(uuid)) {
            if (dataplayer.getData(uuid).getBoolean("register")) {
                if (args.length == 1) {
                    String haslo1 = dataplayer.getData(uuid).getString("password");
                    String haslo2 = HashPassword.tohash(args[0]);
                    if (haslo1.equals(haslo2)) {
                        p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.login")).replace("&", "§"));
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        InetSocketAddress IPAdressPlayer = p.getAddress();
                        String ip = IPAdressPlayer.toString();
                        int entityTypeLenght = ip.length() - 6;
                        String ipgracza = ip.substring(0, entityTypeLenght);
                        data.getData().set("sessions." + p.getName() + ".date", format.format(now));
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        data.getData().set("sessions." + p.getName() + ".ip", ipgracza);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dataplayer.getData(uuid).set("lastlogin", format.format(now));
                        try {
                            dataplayer.saveData(uuid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dataplayer.getData(uuid).set("notifications", false);
                        try {
                            dataplayer.saveData(uuid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        plugin.LoggedIn.put(uuid, true);
                        if (plugin.getConfig().getBoolean("safelocation")) {
                            double x = dataplayer.getData(uuid).getDouble("lastlocation.x");
                            double y = dataplayer.getData(uuid).getDouble("lastlocation.y");
                            double z = dataplayer.getData(uuid).getDouble("lastlocation.z");
                            float yaw = dataplayer.getData(uuid).getInt("lastlocation.yaw");
                            float pitch = dataplayer.getData(uuid).getInt("lastlocation.pitch");
                            String world = dataplayer.getData(uuid).getString("lastlocation.world");
                            p.teleport(new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch));
                        }
                        if (plugin.getConfig().getBoolean("logs")) {
                            Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.login")).replace("&", "§").replace("{player}", p.getName()), true);
                        }
                    } else {
                        p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.the_password_is_incorrect")).replace("&", "§"));
                    }
                } else {
                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.the_password_is_incorrect")).replace("&", "§"));
                }
            } else {
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.use_register")).replace("&", "§"));
            }
        } else {
            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.logged_in_already")).replace("&", "§"));
        }
        return false;
    }
}
