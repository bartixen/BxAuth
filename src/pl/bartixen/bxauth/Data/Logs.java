package pl.bartixen.bxauth.Data;

import pl.bartixen.bxauth.Main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class Logs {

    public static void logDebug(String message, Boolean alert) {
        try {
            File dataFolder = Main.getPlugin(Main.class).getDataFolder();
            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File saveTo = new File(Main.getPlugin(Main.class).getDataFolder(), "logs.txt");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(saveTo, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
            String time = dateFormat.format(date);

            printWriter.println(time + " | " + message);
            printWriter.flush();
            printWriter.close();

            if (alert) {
                Main.getPlugin(Main.class).getLogger().log(Level.INFO, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
