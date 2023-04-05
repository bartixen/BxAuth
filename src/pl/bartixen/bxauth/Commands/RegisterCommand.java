package pl.bartixen.bxauth.Commands;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.*;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class RegisterCommand implements CommandExecutor {

    Main plugin;

    static DataPlayerManager dataplayer;
    static DataManager data;
    static DataMessages messages;

    public RegisterCommand(Main m) {
        plugin = m;
        m.getCommand("register").setExecutor(this);
        m.getCommand("zarejestruj").setExecutor(this);
        m.getCommand("reg").setExecutor(this);
        dataplayer = DataPlayerManager.getInstance();
        data = DataManager.getInstance();
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
        if (!(plugin.LoggedIn.get(uuid))) {
            if (!(JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM)) {
                if (!(dataplayer.getData(uuid).getBoolean("register"))) {
                    if (args.length == 3) {
                        String haslo1 = args[0];
                        String haslo2 = args[1];
                        String captcha1 = args[2];
                        String captcha2 = dataplayer.getData(uuid).getString("captcha");
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        if (haslo1.equals(haslo2)) {
                            if (haslo1.length() >= plugin.getConfig().getInt("password.minlength")) {
                                if (haslo1.length() <= plugin.getConfig().getInt("password.maxlenght")) {
                                    List<String> wiadomosci = plugin.getConfig().getStringList("password.unsafePasswords");
                                    for (String blacklist : wiadomosci) {
                                        if (haslo1.equals(blacklist.toLowerCase())) {
                                            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.bad_password")).replace("&", "§"));
                                            break;
                                        }
                                    }
                                    InetSocketAddress IPAdressPlayer = p.getAddress();
                                    String ip = IPAdressPlayer.toString();
                                    int entityTypeLenght = ip.length() - 6;
                                    String ipgracza = ip.substring(0, entityTypeLenght);
                                    if (plugin.getConfig().getBoolean("recoveryCode.enabled")) {
                                        if ((captcha1.toLowerCase(Locale.ROOT).equals(captcha2.toLowerCase(Locale.ROOT)))) {
                                            register(uuid, now, format, p, haslo1, ipgracza);
                                        } else {
                                            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.bad_captcha")).replace("&", "§"));
                                        }
                                    } else {
                                        register(uuid, now, format, p, haslo1, ipgracza);
                                    }
                                } else {
                                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.maximum_number_of_password_characters")).replace("&", "§").replace("{max}", Objects.requireNonNull(plugin.getConfig().getString("password.maxlenght"))));
                                }
                            } else {
                                p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.minimum_number_of_password_characters")).replace("&", "§").replace("{min}", Objects.requireNonNull(plugin.getConfig().getString("password.minlenght"))));
                            }
                        } else {
                            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.passwords_do_not_match")).replace("&", "§"));
                        }
                    } else {
                        String captcha = dataplayer.getData(uuid).getString("captcha");
                        p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.correct_use_of_register")).replace("&", "§").replace("{captcha}", captcha));
                    }
                } else {
                    p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.register_in_already")).replace("&", "§"));
                }
            } else {
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.premium_player_cannot_register")).replace("&", "§"));
            }
        } else {
            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.logged_in_already")).replace("&", "§"));
        }
        return false;
    }

    public void register(UUID uuid, Date now, SimpleDateFormat format, Player p, String haslo1, String ipgracza) {
        dataplayer.getData(uuid).set("data_register", format.format(now));
        try {
            dataplayer.saveData(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dataplayer.getData(uuid).set("register", true);
        try {
            dataplayer.saveData(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dataplayer.getData(uuid).set("password", HashPassword.tohash(haslo1));
        try {
            dataplayer.saveData(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        data.getData().set("sessions." + p.getName() + ".date", format.format(now));
        try {
            data.saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        data.getData().set("sessions." + p.getName() + ".ip", ipgracza);
        try {
            data.saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dataplayer.getData(uuid).set("notifications", false);
        try {
            dataplayer.saveData(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.register")).replace("&", "§"));
        if (plugin.getConfig().getBoolean("logs")) {
            Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.register")).replace("&", "§").replace("{player}", p.getName()), true);
        }
        plugin.LoggedIn.put(uuid, true);
    }
}