package pl.bartixen.bxauth.Data;

import org.bukkit.plugin.Plugin;
import pl.bartixen.bxauth.Main;

import java.io.IOException;

public class CheckFile {

    Main plugin;

    DataMessages messages;

    public CheckFile(Main m) {
        plugin = m;
        messages = DataMessages.getInstance();
    }

    public void check(Plugin plugin) throws IOException {
        if (messages.getData().getString("reload") == null) {
            if (Main.language.equals("pl")) {
                messages.getData().set("authenticate_first", "&7Musisz się najpierw uwierzytelnić");
                messages.getData().set("player.characters_maximum", "&7Twój nick może mieć maksymalnie &9{max} znaków");
                messages.getData().set("player.characters_minimum", "&7Twój nick musi mieć minimum &9{min} znaki");
                messages.getData().set("player.characters_illegal", "&7Twój nick posiada &9niedozwolone znaki");
                messages.getData().set("player.online_player", "&7Gracz &9{player} &7jest już &aonline &7na serwerze");
                messages.getData().set("player.successfully_verified", "&7Zweryfikowano konto pomyślnie, dołącz ponownie na serwer w celu &9rejestracji konta");
                messages.getData().set("player.too_many_accounts", "&7Wykryto zbyt wiele kont na tym adresie IP.\n&7Twoje konta: &9{accounts}");
                messages.getData().set("player.multi_accounts", "&7Na tym adresie IP jest zarejestrowanych wiele kont: &9{accounts}");
                messages.getData().set("player.autologin_session", "&7Zalogowano automatycznie z powodu aktywnej sesji");
                messages.getData().set("player.unregister", "&7Zostałeś pomyślnie wyrejestrowany");
                messages.getData().set("player.use_register", "&7Najpierw użyj: &9/register");
                messages.getData().set("player.use_login", "&7Najpierw użyj: &9/login");
                messages.getData().set("player.login", "&7Zalogowano pomyślnie");
                messages.getData().set("player.premium_player_cannot_unregister", "&7Gracz premium nie może się wyrejestrować");
                messages.getData().set("player.premium_player_cannot_register", "&7Gracz premium nie może się sam zarejestrować");
                messages.getData().set("player.premium_player_cannot_logging_out", "&7Gracz premium nie może się wylogować");
                messages.getData().set("player.premium_player_cannot_changepassword", "&7Gracz premium nie może zmienić hasła");
                messages.getData().set("player.unregistered_player", "&7Gracz &9{player} &7został pomyślnie wyrejestrowany");
                messages.getData().set("player.the_player_is_not_registered", "&7Gracz &9{player} &7nie jest zarejestrowany");
                messages.getData().set("player.logging_out", "&7Zostałeś wylogowany");
                messages.getData().set("player.not_logged_in", "&7Nie jesteś zalogowany");
                messages.getData().set("player.register", "&7Zostałeś zarejestrowany pomyślnie");
                messages.getData().set("player.logged_in_already", "&7Jesteś już zalogowany");
                messages.getData().set("player.register_in_already", "&7Jesteś już zarejestrowany");
                messages.getData().set("player.bad_password", "&7Twoje hasło jest zbyt proste lub znajduje się na liście niedozwolonych haseł");
                messages.getData().set("player.bad_captcha", "&7Podany kod captcha jest niepoprawny");
                messages.getData().set("player.login_time", "&7Twój czas na uwierzytelnienie miną");
                messages.getData().set("player.maximum_number_of_password_characters", "&7Twoje hasło może mieć maksymalnie &9{max} &7znaków");
                messages.getData().set("player.minimum_number_of_password_characters", "&7Twoje hasło musi mieć minimum &9{min} &7znaków");
                messages.getData().set("player.passwords_do_not_match", "&7Podane hasła nie zgadzaja się");
                messages.getData().set("player.correct_use_of_register", "&7Poprawne użycie: &9/register [haslo] [haslo] &e{captcha}");
                messages.getData().set("player.correct_use_of_changepassword", "&7Poprawne użycie: &9/changepassword [stare_hasło] [nowe_hasło]");
                messages.getData().set("player.correct_use_of_multi", "&7Poprawne użycie: &9/multikonta [gracz]");
                messages.getData().set("player.correct_use_of_login", "&7Poprawne użycie: &9/login [hasło]");
                messages.getData().set("player.successfully_change_the_password", "&7Pomyślnie zmieniono hasło");
                messages.getData().set("player.the_password_is_incorrect", "&7Podane hasło jest niepoprawne");
                messages.getData().set("player.no_player_in_base", "&7Nie znaleziono gracza &9{player} &7w bazie danych");
                messages.getData().set("player.the_player_has_a_variable_IP_address", "&7Gracz &9{player} &7ma zmienne IP");
                messages.getData().set("player.cannot_create_multiple_accounts", "&7Od teraz gracz &9{player} &7nie moze tworzyć multikont");
                messages.getData().set("player.can_create_multiple_accounts", "&7Od teraz gracz &9{player} &7może tworzyć multikonta");
                messages.getData().set("player.antibot_disable", "&7Ochrona antybot została &9wyłączona");
                messages.getData().set("player.antibot_enable", "&7Ochrona antybot została &9włączona");
                messages.getData().set("automessage.register", "&7Zarejestruj się przy użyciu: &9/register [hasło] [hasło] &e{captcha}");
                messages.getData().set("automessage.login", "&7Zaloguj się przy użyciu: &9/login [hasło]");
                messages.getData().set("logs.multi_accounts", "&7Gracz &9{player} &7ma wiele kont: &9{accounts}");
                messages.getData().set("logs.login_premium", "Gracz {player} pomyślnie zalogował się kontem premium");
                messages.getData().set("logs.autologin_session", "Gracz {player} pomyślnie zalogował się przez aktywną sesje");
                messages.getData().set("logs.unregister", "Gracz {player} pomyślnie wyrejestrował się");
                messages.getData().set("logs.register", "Gracz {player} pomyślnie zarejestrowal sie");
                messages.getData().set("logs.deregistering", "Gracz {player} pomyślnie został wyrejestrowany przez {sender}");
                messages.getData().set("logs.logging_out", "Gracz {player} pomyślnie się wylogował");
                messages.getData().set("logs.changepassword", "Gracz {player} pomyślnie zmienił hasło");
                messages.getData().set("logs.cannot_create_multiple_accounts", "Gracz {sender} pomyślnie zabrał możliwość tworzenia kont dla {player}");
                messages.getData().set("logs.can_create_multiple_accounts", "Gracz {sender} pomyślnie nadał możliwość tworzenia kont dla {player}");
                messages.getData().set("logs.login", "Gracz {player} pomyślnie zalogował się przez hasło");
                messages.getData().set("logs.antibot_disable", "Ochrona antybot została wyłączona przez {player}");
                messages.getData().set("logs.antibot_enable", "Ochrona antybot została włączona przez {player}");
                messages.getData().set("logs.different_location", "&eGracz &c{player} &ezalogował się z innej lokalizacji");
                messages.getData().set("command_only_for_players", "&cTa komenda jest przeznaczona tylko dla graczy");
                messages.getData().set("no_permissions", "&7Brak permisji: &9{permission}");
                messages.getData().set("reload", "§7Zaloguj się ponownie");
                messages.saveData();
            } else {
                messages.getData().set("authenticate_first", "&7You need to authenticate first");
                messages.getData().set("player.characters_maximum", "&7Your nickname can be up to &9{max} characters long");
                messages.getData().set("player.characters_minimum", "&7Your nickname must be at least &9{min} characters long");
                messages.getData().set("player.characters_illegal", "&7Your nickname has &9illegal characters");
                messages.getData().set("player.online_player", "&7Player &9{player} &7is already &aonline &7on the server");
                messages.getData().set("player.successfully_verified", "&7The account has been verified successfully, join the server again in order to &9account registration");
                messages.getData().set("player.too_many_accounts", "&7Too many accounts have been detected on this address IP.\n&7Your accounts: &9{accounts}");
                messages.getData().set("player.multi_accounts", "&7Multiple accounts are registered on this IP address: &9{accounts}");
                messages.getData().set("player.autologin_session", "&7Logged in automatically due to an active session");
                messages.getData().set("player.unregister", "&7You have been successfully deregistered");
                messages.getData().set("player.use_register", "&7First use: &9/register");
                messages.getData().set("player.use_login", "&7First use: &9/login");
                messages.getData().set("player.login", "&7Logged in successfully");
                messages.getData().set("player.premium_player_cannot_unregister", "&7Premium player cannot unregister");
                messages.getData().set("player.premium_player_cannot_register", "&7Premium player cannot register himself");
                messages.getData().set("player.premium_player_cannot_logging_out", "&7Premium player cannot log out");
                messages.getData().set("player.premium_player_cannot_changepassword", "&7Premium player cannot change the password");
                messages.getData().set("player.unregistered_player", "&7Player &9{player} &7has been successfully unregistered");
                messages.getData().set("player.the_player_is_not_registered", "&7Player &9{player} &7is not registered");
                messages.getData().set("player.logging_out", "&7You have been logged out");
                messages.getData().set("player.not_logged_in", "&7You are not logged in");
                messages.getData().set("player.register", "&7You have been registered successfully");
                messages.getData().set("player.logged_in_already", "&7You are already logged in");
                messages.getData().set("player.register_in_already", "&7You are already registered");
                messages.getData().set("player.bad_password", "&7Your password is too simple or it is on the list of disallowed passwords");
                messages.getData().set("player.bad_captcha", "&7The provided captcha code is invalid");
                messages.getData().set("player.login_time", "&7Your time for authentication will expire");
                messages.getData().set("player.maximum_number_of_password_characters", "&7Your password can have a maximum of &9{max} &7characters");
                messages.getData().set("player.minimum_number_of_password_characters", "&7Your password must be at least &9{min} &7characters");
                messages.getData().set("player.passwords_do_not_match", "&7The given passwords do not match");
                messages.getData().set("player.correct_use_of_register", "&7Correct use: &9/register [password] [password] &e{captcha}");
                messages.getData().set("player.correct_use_of_changepassword", "&7Correct use: &9/changepassword [old_password] [new_password]");
                messages.getData().set("player.correct_use_of_multi", "&7Correct use: &9/multi [player]");
                messages.getData().set("player.correct_use_of_login", "&7Correct use: &9/login [password]");
                messages.getData().set("player.successfully_change_the_password", "&7Password changed successfully");
                messages.getData().set("player.the_password_is_incorrect", "&7The specified password is incorrect");
                messages.getData().set("player.no_player_in_base", "&7Player &9{player} &7not found in the database");
                messages.getData().set("player.the_player_has_a_variable_IP_address", "&7Player &9{player} &7has variable IP");
                messages.getData().set("player.cannot_create_multiple_accounts", "&7From now on, the player &9{player} &7cannot create multi-accounts");
                messages.getData().set("player.can_create_multiple_accounts", "&7From now on, the player &9{player} &7can create multi-accounts");
                messages.getData().set("player.antibot_disable", "&7Antibot protection has been &9disabled");
                messages.getData().set("player.antibot_enable", "&7Antibot protection has been &9enabled");
                messages.getData().set("automessage.register", "&7Register with: &9/register [password] [password] &e{captcha}");
                messages.getData().set("automessage.login", "&7Log in with: &9/login [password]");
                messages.getData().set("logs.multi_accounts", "&7Player &9{player} &7has multiple accounts: &9{accounts}");
                messages.getData().set("logs.login_premium", "The {player} has successfully logged in with the premium account");
                messages.getData().set("logs.autologin_session", "The {player} has successfully logged in through the active session");
                messages.getData().set("logs.unregister", "The {player} has successfully unsubscribed");
                messages.getData().set("logs.register", "The {player} has successfully registered");
                messages.getData().set("logs.deregistering", "Player {player} has been successfully unregistered by {sender}");
                messages.getData().set("logs.logging_out", "The {player} has logged out successfully");
                messages.getData().set("logs.changepassword", "The {player} has successfully changed the password");
                messages.getData().set("logs.cannot_create_multiple_accounts", "The {sender} player successfully took the ability to create accounts for {player}");
                messages.getData().set("logs.can_create_multiple_accounts", "The {sender} player successfully granted the ability to create accounts for {player}");
                messages.getData().set("logs.login", "The {player} has successfully logged in with the password");
                messages.getData().set("logs.antibot_disable", "Antibot protection has been disabled by {player}");
                messages.getData().set("logs.antibot_enable", "Antibot protection was enabled by {player}");
                messages.getData().set("logs.different_location", "&eThe &c{player} &elogged in from a different location");
                messages.getData().set("command_only_for_players", "&cThis command is for players only");
                messages.getData().set("no_permissions", "&7No permissions: &9{permission}");
                messages.getData().set("reload", "&7Please log in again");
                messages.saveData();
           }
        }
    }
}
