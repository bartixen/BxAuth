package com.elchologamer.userlogin.util;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.hooks.AuthPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FastLoginHook implements AuthPlugin<Player> {

    public void register() {
        FastLoginBukkit fastLogin = JavaPlugin.getPlugin(FastLoginBukkit.class);
        fastLogin.getCore().setAuthPluginHook((AuthPlugin<Player>) new FastLoginHook());
    }

    @Override
    public boolean forceLogin(Player player) {
        return true;
    }

    @Override
    public boolean forceRegister(Player player, String password) {
        try {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isRegistered(String playerName) {

        return false;
    }
}