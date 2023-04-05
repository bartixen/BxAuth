package pl.bartixen.bxauth.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import pl.bartixen.bxauth.Data.DataManager;
import pl.bartixen.bxauth.Data.DataPlayerManager;
import pl.bartixen.bxauth.Data.DataMessages;
import pl.bartixen.bxauth.Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerEvent implements Listener {

    Main plugin;

    DataPlayerManager dataplayer;
    DataManager data;
    DataMessages messages;

    HashMap<Player, Long> antyspam = new HashMap<Player, Long>();

    public PlayerEvent(Main m) {
        plugin = m;
        data = DataManager.getInstance();
        dataplayer = DataPlayerManager.getInstance();
        messages = DataMessages.getInstance();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCheckNick(AsyncPlayerPreLoginEvent e) throws IOException {
        int min = plugin.getConfig().getInt("minNicknameLength");
        int max = plugin.getConfig().getInt("maxNicknameLength");
        if (e.getName().length() > max) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.characters_maximum")).replace("&", "§").replace("{min}", new String(String.valueOf(max)) + "\n"));
        }
        if (e.getName().length() < min) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.characters_minimum")).replace("&", "§").replace("{min}", new String(String.valueOf(min)) + "\n"));
        }
        String characters = plugin.getConfig().getString("allowedNicknameCharacters");
        if (!(e.getName().matches(characters))) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.characters_illegal")).replace("&", "§") + "\n");
        }
        Player p = Bukkit.getServer().getPlayerExact(e.getName().toLowerCase());
        UUID uuid = e.getUniqueId();
        if (p != null) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.online_player")).replace("&", "§") + "\n");
        }
        if ((dataplayer.getData(uuid).getString("antibot")) == null) {
            if (plugin.getConfig().getBoolean("antibot")) {
                dataplayer.getData(uuid).set("antibot", true);
                dataplayer.saveData(uuid);
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n§8§l[§9§lBxAuth§8§l]\n\n" + Objects.requireNonNull(messages.getData().getString("player.successfully_verified")).replace("&", "§") + "\n");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        List<String> wlkomendy = plugin.getConfig().getStringList("allowcommands");
        String polecenie = e.getMessage();
        String[] cmd = polecenie.substring(1).split(" ", 2);
        if (!(plugin.LoggedIn.get(uuid))) {
            if (!wlkomendy.contains(cmd[0])) {
                e.setCancelled(true);
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("authenticate_first")).replace("&", "§"));

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (!(plugin.LoggedIn.get(uuid))) {
            if (plugin.getConfig().getBoolean("allowChat")) {
                e.setCancelled(true);
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("authenticate_first")).replace("&", "§"));
            }
        }
    }

    public void antyspamcheck(Player p) {
        UUID uuid = p.getUniqueId();
        if ((antyspam.containsKey(p)) && dataplayer.getData(uuid).getBoolean("notifications")) {
            if (antyspam.get(p) > System.currentTimeMillis()) {
                antyspam.put(p, System.currentTimeMillis() + 6 * 1000);
            } else {
                p.sendMessage(Objects.requireNonNull(messages.getData().getString("authenticate_first")).replace("&", "§"));
                antyspam.put(p, System.currentTimeMillis() + 6 * 1000);
            }
        } else {
            p.sendMessage(Objects.requireNonNull(messages.getData().getString("authenticate_first")).replace("&", "§"));
            antyspam.put(p, System.currentTimeMillis() + 6 * 1000);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        int x1 = e.getFrom().getBlockX();
        int x2 = e.getTo().getBlockX();
        int z1 = e.getFrom().getBlockZ();
        int z2 = e.getTo().getBlockZ();
        if (!(plugin.LoggedIn.get(uuid))) {
            if (!(x1 == x2 && z1 == z2)) {
                e.setCancelled(true);
                antyspamcheck(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (!(plugin.LoggedIn.get(uuid))) {
            e.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (!(plugin.LoggedIn.get(uuid))) {
            e.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();

        if (!(plugin.LoggedIn.get(uuid))) {
            e.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler
    public void onGod(EntityDamageEvent e) {
        if (e.getEntityType() != EntityType.PLAYER)
            return;
        if ((plugin.LoggedIn.get(e.getEntity().getUniqueId())))
            return;
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerShear(PlayerShearEntityEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Player p = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerHeldItem(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            event.setCancelled(true);
            antyspamcheck(p);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInventoryOpen(InventoryOpenEvent event) {
        final HumanEntity player = event.getPlayer();
        Player p = (Player) event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (!(plugin.LoggedIn.get(uuid))) {
            if (dataplayer.getData(uuid).getString("check_account") != null) {
                event.setCancelled(true);
            }
            antyspamcheck(p);
        }
    }
}
