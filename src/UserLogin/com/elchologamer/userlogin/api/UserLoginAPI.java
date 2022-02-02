package com.elchologamer.userlogin.api;

import org.bukkit.entity.Player;

import java.util.UUID;

public class UserLoginAPI {

    public static boolean isRegistered(Player player) {
        return false;
    }

    public static boolean isRegistered(UUID uuid) {
        return false;
    }

    @Deprecated
    public static boolean isLoggedIn(String player) {
        return false;
    }

    public static boolean isLoggedIn(Player player) {
        return false;
    }

    public static boolean isLoggedIn(UUID uuid) {
        return false;
    }
}
