package pl.bartixen.bxauth.Listeners;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class LogFilter implements Filter {

    public static void register() {
        Logger logger = ((Logger) LogManager.getRootLogger());
        logger.addFilter(new LogFilter());
    }

    private Result filter(String message) {
        if ((message.matches("(.+) issued server command: /(?i)(login|zaloguj|register|reg|zarejestruj|changepass|changepassword)(.*)")) || (message.matches("(.+) has a verified premium account: (.*)")) || (message.matches("(.+) Twój nick moźe mieć maksymalnie (.*)")) || (message.matches("(.+) Twój nick musi mieć minimum (.*)")) || (message.matches("(.+) Twój nick posiada niedozwolone znaki (.*)")) || (message.matches("(.+) jest już online na serwerze (.*)")) || (message.matches("(.+) Zweryfikowano konto pomyślnie, dołącz ponownie na serwer w celu rejestracji konta. (.*)")) || (message.matches("(.+) Wykryto zbyt wiele kont na tym adresie IP. (.*)"))) {
            return Result.DENY;
        } else {
            return Result.NEUTRAL;
        }
    }

    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String s, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return filter(s);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
        return filter(o.toString());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
        return filter(message.getFormattedMessage());
    }

    @Override
    public Result filter(LogEvent e) {
        return filter(e.getMessage().getFormattedMessage());
    }

    @Override
    public State getState() {
        return State.STARTED;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}