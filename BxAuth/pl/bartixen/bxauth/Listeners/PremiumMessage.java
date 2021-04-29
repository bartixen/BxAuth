package pl.bartixen.bxauth.Listeners;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Main;

import java.util.UUID;

public class PremiumMessage implements Listener {

    static Main plugin;

    public PremiumMessage(Main m) {
        plugin = m;
    }

    @EventHandler
    public void JoinPlayer(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) != PremiumStatus.PREMIUM) {
                    if (!(plugin.LoggedIn.get(uuid))) {
                        BossPremium(p);
                    }
                }
            }
        }, 16);
    }

    public static void BossPremium(Player p) {
        BossBar bar = Bukkit.createBossBar("§f§lJesteś graczem §9§lPREMIUM §f§lużyj: §9§l/premium", BarColor.BLUE, BarStyle.SEGMENTED_10);
        bar.setVisible(true);
        bar.addPlayer(p);
        bar.setProgress(1.0D);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                bar.setProgress(0.9D);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        bar.setProgress(0.8D);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                bar.setProgress(0.7D);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    public void run() {
                                        bar.setProgress(0.6D);
                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                            public void run() {
                                                bar.setProgress(0.5D);
                                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                    public void run() {
                                                        bar.setProgress(0.4D);
                                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                            public void run() {
                                                                bar.setProgress(0.3D);
                                                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                                    public void run() {
                                                                        bar.setProgress(0.2D);
                                                                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                                            public void run() {
                                                                                bar.setProgress(0.1D);
                                                                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                                                    public void run() {
                                                                                        bar.setProgress(0.0D);
                                                                                        bar.removePlayer(p);
                                                                                    }
                                                                                }, 10);
                                                                            }
                                                                        }, 10);
                                                                    }
                                                                }, 10);
                                                            }
                                                        }, 10);
                                                    }
                                                }, 10);
                                            }
                                        }, 10);
                                    }
                                }, 10);
                            }
                        }, 10);
                    }
                }, 10);
            }
        }, 10);
    }
}
