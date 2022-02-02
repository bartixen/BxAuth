package com.elchologamer.userlogin;

import com.elchologamer.userlogin.util.FastLoginHook;
import org.bukkit.plugin.java.JavaPlugin;

public class UserLogin extends JavaPlugin {

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("FastLogin")) {
            new FastLoginHook().register();
        }
    }
}
