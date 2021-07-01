package pl.bartixen.bxauth.Listeners;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.PremiumStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.ItemBuilder;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class CheckAccount implements Listener {

    static Main plugin;

    static DataManager data;

    public CheckAccount(Main m) {
        plugin = m;
        data = DataManager.getInstance();
    }

    @EventHandler
    public void JoinPlayer(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (data.getData().getString(uuid + ".check_account") == null) {
                    if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) != PremiumStatus.PREMIUM) {
                        CheckAccount(p);
                    }
                }
            }
        }, 16);
    }

    @EventHandler
    public void onOpenMenu(InventoryClickEvent e) throws IOException {
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        if (e.getView().getTitle().equals("§9§lWYBIERZ STATUS KONTA")) {
            e.setCancelled(true);
            if (e.getRawSlot() == 11) {
                p.closeInventory();
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "premium " + p.getDisplayName());
                Date now = new Date();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                data.getData().set(uuid + ".check_account", true);
                data.getData().set(uuid + ".premium", true);
                data.getData().set(uuid + ".register", true);
                data.getData().set(uuid + ".data_register", format.format(now));
                data.saveData();
                p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n§7Ustawiono status konta na §fPREMIUM§7.\n");
            }
            if (e.getRawSlot() == 15) {
                p.closeInventory();
                data.getData().set(uuid + ".check_account", false);
                data.getData().set(uuid + ".premium", false);
                data.saveData();
                p.kickPlayer("\n§8§l[§9§lBxAuth§8§l]\n\n§7Ustawiono status konta na §fNOPREMIUM§7.\n");
                plugin.getLogger().log(Level.INFO, "Gracz " + p.getName() + " pomyslnie wyrejestrowal sie");
            }
        }
    }

    @EventHandler
    private void guiClose(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (data.getData().getString(uuid + ".check_account") == null) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    if ((JavaPlugin.getPlugin(FastLoginBukkit.class).getStatus(p.getUniqueId())) != PremiumStatus.PREMIUM) {
                        CheckAccount(p);
                    }
                }
            }, 5);
        }
    }

    public static void CheckAccount(Player p) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder) p, 27, ("§9§lWYBIERZ STATUS KONTA"));

        ItemBuilder slot11 = (new ItemBuilder(Material.LIME_CONCRETE, 1)).setTitle("§a§lKONTO PREMIUM");
        inventory.setItem(11, slot11.build());

        ItemBuilder slot15 = (new ItemBuilder(Material.RED_CONCRETE, 1)).setTitle("§c§lKONTO NOPREMIUM");
        inventory.setItem(15, slot15.build());

        ItemBuilder backguard = (new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)).setTitle("§7");
        inventory.setItem(0, backguard.build());
        inventory.setItem(1, backguard.build());
        inventory.setItem(2, backguard.build());
        inventory.setItem(3, backguard.build());
        inventory.setItem(4, backguard.build());
        inventory.setItem(5, backguard.build());
        inventory.setItem(6, backguard.build());
        inventory.setItem(7, backguard.build());
        inventory.setItem(8, backguard.build());
        inventory.setItem(9, backguard.build());
        inventory.setItem(10, backguard.build());
        inventory.setItem(12, backguard.build());
        inventory.setItem(13, backguard.build());
        inventory.setItem(14, backguard.build());
        inventory.setItem(16, backguard.build());
        inventory.setItem(17, backguard.build());
        inventory.setItem(18, backguard.build());
        inventory.setItem(19, backguard.build());
        inventory.setItem(20, backguard.build());
        inventory.setItem(21, backguard.build());
        inventory.setItem(22, backguard.build());
        inventory.setItem(23, backguard.build());
        inventory.setItem(24, backguard.build());
        inventory.setItem(25, backguard.build());
        inventory.setItem(26, backguard.build());
        p.openInventory(inventory);
    }


}
