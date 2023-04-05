package pl.bartixen.bxauth.Listeners;

import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogFilter {

    private static void suppressUselessLogging() {
        for (Handler handler : Logger.getLogger("").getHandlers()) {
            handler.setFilter(new Filter() {
                @Override
                public boolean isLoggable(LogRecord message) {
                    boolean result = true;
                    if ((
                            message.getMessage().matches("(.+) issued server command: /(?i)(login|zaloguj|register|reg|zarejestruj|changepass|changepassword)(.*)"))
                            || (message.getMessage().matches("(.+) has a verified premium account: (.*)"))
                            || (message.getMessage().matches("(.+) Twój nick moźe mieć maksymalnie (.*)"))
                            || (message.getMessage().matches("(.+) Twój nick musi mieć minimum (.*)"))
                            || (message.getMessage().matches("(.+) Twój nick posiada niedozwolone znaki (.*)"))
                            || (message.getMessage().matches("(.+) jest już online na serwerze (.*)"))
                            || (message.getMessage().matches("(.+) Zweryfikowano konto pomyślnie, dołącz ponownie na serwer w celu rejestracji konta. (.*)"))
                            || (message.getMessage().matches("(.+) Wykryto zbyt wiele kont na tym adresie IP. (.*)"))) {
                        result = false;
                    }
                    return result;
                }
            });
        }
    }
}