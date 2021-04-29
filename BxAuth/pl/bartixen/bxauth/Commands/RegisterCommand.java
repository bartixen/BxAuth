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

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class RegisterCommand implements CommandExecutor {

    Main plugin;

    static DataManager data;

    public RegisterCommand(Main m) {
        plugin = m;
        m.getCommand("register").setExecutor(this);
        m.getCommand("zarejestruj").setExecutor(this);
        m.getCommand("reg").setExecutor(this);
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
            if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) != PremiumStatus.PREMIUM) {
                if (!(data.getData().getBoolean(uuid + ".register"))) {
                    if (args.length == 3) {
                        String haslo1 = args[0];
                        String haslo2 = args[1];
                        String captcha1 = args[2];
                        String captcha2 = data.getData().getString(uuid + ".captcha");
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        if (haslo1.equals(haslo2)) {
                            if (haslo1.length() >= plugin.getConfig().getInt("password.minlength")) {
                                if (haslo1.length() <= plugin.getConfig().getInt("password.maxlenght")) {
                                    List<String> wiadomosci = plugin.getConfig().getStringList("password.unsafePasswords");
                                    for (String blacklist : wiadomosci) {
                                        if (haslo1.equals(blacklist.toLowerCase())) {
                                            p.sendMessage("§7Twoje haslo jest zbyt proste lub znajduje się na liście niedozwolonych haseł");
                                            break;
                                        }
                                    }
                                    InetSocketAddress IPAdressPlayer = p.getAddress();
                                    String ip = IPAdressPlayer.toString();
                                    int entityTypeLenght = ip.length() - 6;
                                    String ipgracza = ip.substring(0, entityTypeLenght);
                                    if (plugin.getConfig().getBoolean("recoveryCode.enabled")) {
                                        if ((captcha1.toLowerCase(Locale.ROOT).equals(captcha2.toLowerCase(Locale.ROOT)))) {
                                            data.getData().set(uuid + ".data_register", format.format(now));
                                            data.getData().set(uuid + ".register", true);
                                            data.getData().set(uuid + ".password", HashPassword.tohash(haslo1));
                                            data.getData().set("sessions." + p.getName() + ".date", format.format(now));
                                            data.getData().set("sessions." + p.getName() + ".ip", ipgracza);
                                            data.getData().set(uuid + ".notifications", false);
                                            data.getData();
                                            p.sendMessage("§7Zostaleś zarejestrowany pomyślnie");
                                            plugin.LoggedIn.put(uuid, true);
                                        } else {
                                            p.sendMessage("§7Podany kod captcha jest niepoprawny");
                                        }
                                    } else {
                                        data.getData().set(uuid + ".data_register", format.format(now));
                                        data.getData().set(uuid + ".register", true);
                                        data.getData().set(uuid + ".password", HashPassword.tohash(haslo1));
                                        data.getData().set("sessions." + p.getName() + ".date", format.format(now));
                                        data.getData().set("sessions." + p.getName() + ".ip", ipgracza);
                                        data.getData();
                                        p.sendMessage("§7Zostaleś zarejestrowany pomyślnie");
                                        plugin.LoggedIn.put(uuid, true);
                                    }
                                } else {
                                    p.sendMessage("§7Twoje haslo może mieć maksymalnie §9" + plugin.getConfig().getInt("password.maxlenght") + " §7znaków");
                                }
                            } else {
                                p.sendMessage("§7Twoje haslo musi mieć minimum §9" + plugin.getConfig().getInt("password.minlength") + " §7znaków");
                            }
                        } else {
                            p.sendMessage("§7Podane hasla nie zgadzają się");
                        }
                    } else {
                        String captcha = data.getData().getString(uuid + ".captcha");
                        p.sendMessage("§7Poprawne użycie: §9/register [haslo] [haslo] " + captcha);
                    }
                } else {
                    p.sendMessage("§7Jesteś już zarejestrowany");
                }
            } else {
                p.sendMessage("§7Gracz premium nie może się sam zarejestrować");
            }
        } else {
            p.sendMessage("§7Jesteś już zalogowany");
        }

        return false;
    }
}