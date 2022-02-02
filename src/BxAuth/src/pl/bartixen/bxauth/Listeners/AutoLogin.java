package pl.bartixen.bxauth.Listeners;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class AutoLogin implements Listener {

    static Main plugin;

    public ArrayList<String> adresyipgraczy = new ArrayList<>();

    static DataManager data;

    static String seconds;

    static String minutes;

    static String hours;

    static String days;

    static String seconds1;

    static String minutes1;

    static String hours1;

    static String days1;

    static int leftSide;

    static int rightSide;

    public AutoLogin(Main m) {
        plugin = m;
        data = DataManager.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        InetSocketAddress IPAdressPlayer = p.getAddress();
        String ip = IPAdressPlayer.toString();
        int entityTypeLenght = ip.length() - 6;
        String ipgracza = ip.substring(0, entityTypeLenght);
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        double x = plugin.getConfig().getDouble("teleportspawn.x");
        double y = plugin.getConfig().getDouble("teleportspawn.y");
        double z = plugin.getConfig().getDouble("teleportspawn.z");
        float yaw = plugin.getConfig().getInt("teleportspawn.yaw");
        float pitch = plugin.getConfig().getInt("teleportspawn.pitch");
        String world = plugin.getConfig().getString("teleportspawn.world");
        String ipdospr = ipgracza.replace("/", "").replace(".", "");
        adresyipgraczy = new ArrayList<>(data.getData().getStringList("useripregister." + ipdospr + ".gracze"));

        plugin.LoggedIn.put(uuid, false);
        data.getData().set(uuid + ".logintime", 0);
        data.getData().set(uuid + ".notifications", false);
        data.getData().set(uuid + ".last_ip", ipgracza);
        data.getData().set(p.getName() + ".uuid", uuid.toString());
        data.getData().set(uuid + ".captcha", captcha(plugin.getConfig().getInt("recoveryCode.length")));
        data.saveData();

        if ((data.getData().getString("useripregister." + ipdospr)) == null) {
            adresyipgraczy.add(p.getName());
            data.getData().set("useripregister." + ipdospr + ".gracze", adresyipgraczy);
            data.getData().set("useripregister." + ipdospr + ".ip", ipgracza);
            data.getData().set("useripregister." + ipdospr + ".multikonta", false);
            data.saveData();
        } else {
            if (!(data.getData().getBoolean("useripregister." + ipdospr + ".multikonta"))) {
                if (adresyipgraczy.size() > plugin.getConfig().getInt("maxIPregister")) {
                    if (!(adresyipgraczy.contains(p.getDisplayName()))) {
                        p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n§7Wykryto zbyt wiele kont na tym adresie IP.\n§7Twoje konta: " + adresyipgraczy.toString() + "\n\n");
                        return;
                    }
                } else {
                    if (!(adresyipgraczy.contains(p.getDisplayName()))) {
                        adresyipgraczy.add(p.getName());
                        data.getData().set("useripregister." + ipdospr + ".gracze", adresyipgraczy);
                        data.saveData();
                    }
                }

            }
        }
        if (adresyipgraczy.size() > 1) {
            p.sendMessage("§7Na tym adresie IP jest zarejestrowanych wiele kont: §9" + adresyipgraczy);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("bxauth.notifications.multi") || player.isOp()) {
                    player.sendMessage("§7Gracz §9" + p.getName() + " §7ma wiele kont: §9" + adresyipgraczy);
                }
            }
        }

        if ((data.getData().getString(uuid + ".first_ip")) == null) {
            data.getData().set(uuid + ".first_ip", ipgracza);
            data.getData().set(uuid + ".data_first_join", format.format(now));
            data.getData().set(uuid + ".register", false);
            data.getData().set(uuid + ".lastplay", format.format(now));
            data.getData().set(uuid + ".premium", false);
            if (plugin.getConfig().getBoolean("safelocation")) {
                data.getData().set(uuid + ".lastlocation.world", world);
                data.getData().set(uuid + ".lastlocation.x", x);
                data.getData().set(uuid + ".lastlocation.y", y);
                data.getData().set(uuid + ".lastlocation.z", z);
                data.getData().set(uuid + ".lastlocation.yaw", yaw);
                data.getData().set(uuid + ".lastlocation.pitch", pitch);
                data.saveData();
            }
        }

        data.saveData();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) == PremiumStatus.PREMIUM) {
                    plugin.LoggedIn.put(uuid, true);
                    data.getData().set(uuid + ".premium", true);
                    if (!data.getData().getBoolean(uuid + ".register")) {
                        data.getData().set(uuid + ".register", true);
                        data.getData().set(uuid + ".data_register", format.format(now));
                        data.getData().set(uuid + ".lastlogin", format.format(now));
                    }
                    try {
                        data.saveData();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    safelocation(uuid, p);
                    if (plugin.getConfig().getBoolean("logs")) {
                        plugin.getLogger().log(Level.INFO, "Gracz " + p.getDisplayName() + " pomyslnie zalogowal sie kontem premium");
                    }
                    plugin.LoggedIn.put(uuid, true);
                } else {
                    if (plugin.getConfig().getBoolean("sessions.enabled")) {
                        if (ipgracza.equals(data.getData().getString("sessions." + p.getName() + ".ip"))) {
                            try {
                                if (AutoLogin.check(p.getName())) {
                                    plugin.LoggedIn.put(uuid, true);
                                    p.sendMessage("§7Zalogowano automatycznie z powodu aktywnej sesji");
                                    if (plugin.getConfig().getBoolean("logs")) {
                                        plugin.getLogger().log(Level.INFO, "Gracz " + p.getDisplayName() + " pomyslnie zalogowal sie przez aktywna sesje");
                                    }
                                    data.getData().set(uuid + ".lastlogin", format.format(now));
                                    data.saveData();
                                    safelocation(uuid ,p);
                                } else {
                                    plugin.LoggedIn.put(uuid, false);
                                    data.getData().set(uuid + ".notifications", true);
                                    data.saveData();
                                    teleportspawn(p);
                                }
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            plugin.LoggedIn.put(uuid, false);
                            data.getData().set(uuid + ".notifications", true);
                            try {
                                data.saveData();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            teleportspawn(p);
                        }
                    } else {
                        plugin.LoggedIn.put(uuid, false);
                        data.getData().set(uuid + ".notifications", true);
                        try {
                            data.saveData();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        teleportspawn(p);
                    }
                }
            }
        }, 16);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) == PremiumStatus.PREMIUM) {
                    if (!(plugin.LoggedIn.get(uuid))) {
                        plugin.LoggedIn.put(uuid, true);
                        if (plugin.getConfig().getBoolean("logs")) {
                            plugin.getLogger().log(Level.INFO, "Gracz " + p.getDisplayName() + " pomyslnie zalogowal sie kontem premium");
                        }
                        data.getData().set(uuid + ".notifications", false);
                        data.getData().set(uuid + ".premium", true);
                        safelocation(uuid, p);
                        data.getData().set(uuid + ".lastlogin", format.format(now));
                        try {
                            data.saveData();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        }, 25);
    }

    public void safelocation(UUID uuid, Player p) {
        if (plugin.getConfig().getBoolean("safelocation")) {
            double x = data.getData().getDouble(uuid + ".lastlocation.x");
            double y = data.getData().getDouble(uuid + ".lastlocation.y");
            double z = data.getData().getDouble(uuid + ".lastlocation.z");
            float yaw = data.getData().getInt(uuid + ".lastlocation.yaw");
            float pitch = data.getData().getInt(uuid + ".lastlocation.pitch");
            String world = data.getData().getString(uuid + ".lastlocation.world");
            p.teleport(new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch));
        }
    }

    public void teleportspawn(Player p) {
        if (plugin.getConfig().getBoolean("teleportspawn.enabled")) {
            double x = plugin.getConfig().getDouble("teleportspawn.x");
            double y = plugin.getConfig().getDouble("teleportspawn.y");
            double z = plugin.getConfig().getDouble("teleportspawn.z");
            float yaw = plugin.getConfig().getInt("teleportspawn.yaw");
            float pitch = plugin.getConfig().getInt("teleportspawn.pitch");
            String world = plugin.getConfig().getString("teleportspawn.world");
            p.teleport(new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws IOException {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();
        float yaw = p.getLocation().getYaw();
        float pitch = p.getLocation().getPitch();
        String world = p.getWorld().getName();
        if (plugin.getConfig().getBoolean("safelocation") && (plugin.LoggedIn.get(uuid))) {
            data.getData().set(uuid + ".lastlocation.world", world);
            data.getData().set(uuid + ".lastlocation.x", x);
            data.getData().set(uuid + ".lastlocation.y", y);
            data.getData().set(uuid + ".lastlocation.z", z);
            data.getData().set(uuid + ".lastlocation.yaw", yaw);
            data.getData().set(uuid + ".lastlocation.pitch", pitch);
            data.getData().set(uuid + ".lastplay", format.format(now));
            data.saveData();
        }
    }

    public static String captcha(int n)
    {
        char[] bazowe = new char[n];

        for (int i = 0; i <n; i++)
        {
            bazowe[i] = (char) (((int)(Math.random() * 26)) + (int)'A');
        }

        return (new String(bazowe, 0, n));
    }

    public static boolean check(String player) throws IOException {
        String banDate = data.getData().getString("sessions." + player + ".date");
        banDate = banDate.replace(":", " ").replace("-", " ");
        String[] newString = banDate.split(" ");
        seconds = newString[2];
        minutes = newString[1];
        hours = newString[0];
        days = newString[3];
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String date = format.format(now);
        date = date.replace(":", " ").replace("-", " ");
        String[] newString1 = date.split(" ");
        seconds1 = newString1[2];
        minutes1 = newString1[1];
        hours1 = newString1[0];
        days1 = newString1[3];
        String until = plugin.getConfig().getString("sessions.timeout") + "m";
        String integer1 = until.replaceAll("[^\\d.]", "");
        int timeUntilUnban = Integer.parseInt(integer1);
        if (!until.contains("s"))
            if (until.contains("m")) {
                timeUntilUnban *= 60;
            } else if (until.contains("h")) {
                timeUntilUnban = timeUntilUnban * 60 * 60;
            } else if (until.contains("d")) {
                timeUntilUnban = timeUntilUnban * 60 * 60 * 24;
            }
        int seconds2 = Integer.parseInt(seconds);
        int minutes2 = Integer.parseInt(minutes);
        int hours2 = Integer.parseInt(hours);
        int days2 = Integer.parseInt(days);
        int seconds3 = Integer.parseInt(seconds1);
        int minutes3 = Integer.parseInt(minutes1);
        int hours3 = Integer.parseInt(hours1);
        int days3 = Integer.parseInt(days1);
        leftSide = seconds2 + minutes2 * 60 + hours2 * 60 * 60 + days2 * 60 * 60 * 24 + timeUntilUnban;
        rightSide = seconds3 + minutes3 * 60 + hours3 * 60 * 60 + days3 * 60 * 60 * 24;
        if (leftSide >= rightSide)
            return true;
        data.getData().set("sessions." + player, null);
        data.saveData();
        return false;
    }

}
