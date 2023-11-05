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
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Data.Logs;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class AutoLogin implements Listener {

    static Main plugin;

    public ArrayList<String> adresyipgraczy = new ArrayList<>();

    DataPlayerManager dataplayer;
    static DataManager data;
    static DataMessages messages;

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

    static String ipgracza = "";

    Date now = new Date();
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    public AutoLogin(Main m) {
        plugin = m;
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        InetSocketAddress IPAdressPlayer = p.getAddress();
        String ip = IPAdressPlayer.toString();
        int entityTypeLenght = ip.length() - 6;
        ipgracza = ip.substring(0, entityTypeLenght);
        double x = plugin.getConfig().getDouble("teleportspawn.x");
        double y = plugin.getConfig().getDouble("teleportspawn.y");
        double z = plugin.getConfig().getDouble("teleportspawn.z");
        float yaw = plugin.getConfig().getInt("teleportspawn.yaw");
        float pitch = plugin.getConfig().getInt("teleportspawn.pitch");
        String world = plugin.getConfig().getString("teleportspawn.world");
        String ipdospr = ipgracza.replace("/", "").replace(".", "");
        adresyipgraczy = new ArrayList<>(data.getData().getStringList("useripregister." + ipdospr + ".gracze"));
        plugin.LoggedIn.put(uuid, false);
        dataplayer.getData(uuid).set("logintime", 0);
        dataplayer.saveData(uuid);
        dataplayer.getData(uuid).set("notifications", false);
        dataplayer.saveData(uuid);
        data.getData().set(p.getName() + ".uuid", uuid.toString());
        data.saveData();
        dataplayer.getData(uuid).set("captcha", captcha(plugin.getConfig().getInt("recoveryCode.length")));
        dataplayer.saveData(uuid);
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
                        p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.too_many_accounts")).replace("&", "§").replace("{accounts}", adresyipgraczy.toString()) + "\n\n");
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
            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.multi_accounts")).replace("&", "§").replace("{accounts}", adresyipgraczy.toString()));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("bxauth.notifications.multi") || player.isOp()) {
                    player.sendMessage(Objects.requireNonNull(messages.getData().getString("logs.multi_accounts")).replace("&", "§").replace("{accounts}", adresyipgraczy.toString()).replace("{player}", p.getName()));
                }
            }
        }
        if ((dataplayer.getData(uuid).getString("first_ip")) == null) {
            dataplayer.getData(uuid).set("first_ip", ipgracza);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("data_first_join", format.format(now));
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("register", false);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastplay", format.format(now));
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("premium", false);
            dataplayer.saveData(uuid);
            if (plugin.getConfig().getBoolean("safelocation")) {
                dataplayer.getData(uuid).set("lastlocation.world", world);
                dataplayer.saveData(uuid);
                dataplayer.getData(uuid).set("lastlocation.x", x);
                dataplayer.saveData(uuid);
                dataplayer.getData(uuid).set("lastlocation.y", y);
                dataplayer.saveData(uuid);
                dataplayer.getData(uuid).set("lastlocation.z", z);
                dataplayer.saveData(uuid);
                dataplayer.getData(uuid).set("lastlocation.yaw", yaw);
                dataplayer.saveData(uuid);
                dataplayer.getData(uuid).set("lastlocation.pitch", pitch);
                dataplayer.saveData(uuid);
            }
        } else {
            if (!(ipgracza.equals(dataplayer.getData(uuid).getString("last_ip")))) {
                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.different_location")).replace("&", "§").replace("{player}", p.getName()), true);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("bxauth.notifications.different_location") || player.isOp()) {
                        player.sendMessage(Objects.requireNonNull(messages.getData().getString("logs.different_location")).replace("&", "§").replace("{player}", p.getName()));
                    }
                }
            }
        }
        dataplayer.getData(uuid).set("last_ip", ipgracza);
        dataplayer.saveData(uuid);

        check(p, uuid);
    }

    public void check(Player p, UUID uuid) {
        if (JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.PREMIUM) {
            plugin.LoggedIn.put(uuid, true);
            dataplayer.getData(uuid).set("premium", true);
            try {
                dataplayer.saveData(uuid);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!dataplayer.getData(uuid).getBoolean("register")) {
                dataplayer.getData(uuid).set("register", true);
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                dataplayer.getData(uuid).set("data_register", format.format(now));
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                dataplayer.getData(uuid).set(uuid + ".lastlogin", format.format(now));
                try {
                    dataplayer.saveData(uuid);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            safelocation(uuid ,p);
            if (plugin.getConfig().getBoolean("logs")) {
                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.login_premium")).replace("&", "§").replace("{player}", p.getName()), true);
            }
        } else {
            if (JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId()) == PremiumStatus.UNKNOWN) {
                new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            check(p, uuid);
                        }
                    }, 100
                );
            } else {
                if (plugin.getConfig().getBoolean("sessions.enabled")) {
                    if (ipgracza.equals(data.getData().getString("sessions." + p.getName() + ".ip"))) {
                        if (AutoLogin.check(p)) {
                            plugin.LoggedIn.put(uuid, true);
                            p.sendMessage(Objects.requireNonNull(messages.getData().getString("player.autologin_session")).replace("&", "§"));
                            if (plugin.getConfig().getBoolean("logs")) {
                                Logs.logDebug(Objects.requireNonNull(messages.getData().getString("logs.autologin_session")).replace("&", "§").replace("{player}", p.getDisplayName()), true);
                            }
                            safelocation(uuid ,p);
                        } else {
                            plugin.LoggedIn.put(uuid, false);
                            teleportspawn(p, uuid);
                        }
                    } else {
                        plugin.LoggedIn.put(uuid, false);
                        teleportspawn(p, uuid);
                    }
                } else {
                    plugin.LoggedIn.put(uuid, false);
                    teleportspawn(p, uuid);
                }
            }
        }
    }

    public void safelocation(UUID uuid, Player p) {
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (plugin.getConfig().getBoolean("safelocation")) {
                    double x = dataplayer.getData(uuid).getDouble("lastlocation.x");
                    double y = dataplayer.getData(uuid).getDouble("lastlocation.y");
                    double z = dataplayer.getData(uuid).getDouble("lastlocation.z");
                    float yaw = dataplayer.getData(uuid).getInt("lastlocation.yaw");
                    float pitch = dataplayer.getData(uuid).getInt("lastlocation.pitch");
                    String world = dataplayer.getData(uuid).getString("lastlocation.world");
                    p.teleport(new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch));
                    dataplayer.getData(uuid).set("lastlogin", format.format(now));
                    try {
                        dataplayer.saveData(uuid);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void teleportspawn(Player p, UUID uuid) {
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (plugin.getConfig().getBoolean("teleportspawn.enabled")) {
                    double x = plugin.getConfig().getDouble("teleportspawn.x");
                    double y = plugin.getConfig().getDouble("teleportspawn.y");
                    double z = plugin.getConfig().getDouble("teleportspawn.z");
                    float yaw = plugin.getConfig().getInt("teleportspawn.yaw");
                    float pitch = plugin.getConfig().getInt("teleportspawn.pitch");
                    String world = plugin.getConfig().getString("teleportspawn.world");
                    p.teleport(new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch));
                    dataplayer.getData(uuid).set("notifications", true);
                    try {
                        dataplayer.saveData(uuid);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
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
            dataplayer.getData(uuid).set("lastlocation.world", world);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastlocation.x", x);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastlocation.y", y);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastlocation.z", z);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastlocation.yaw", yaw);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastlocation.pitch", pitch);
            dataplayer.saveData(uuid);
            dataplayer.getData(uuid).set("lastplay", format.format(now));
            dataplayer.saveData(uuid);
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

    public static boolean check(Player player) {
        String banDate = data.getData().getString("sessions." + player.getName() + ".date");
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
        try {
            data.saveData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
