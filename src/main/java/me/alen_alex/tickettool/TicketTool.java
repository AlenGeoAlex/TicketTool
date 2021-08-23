package me.alen_alex.tickettool;

import me.alen_alex.tickettool.configuration.MessageConfiguration;
import me.alen_alex.tickettool.data.DatabaseManager;
import me.alen_alex.tickettool.data.SQLDatabase;
import me.alen_alex.tickettool.database.mysql.MySQL;
import me.alen_alex.tickettool.ticket.TicketManager;
import me.alen_alex.tickettool.validation.Validation;
import me.alen_alex.tickettool.configuration.Configuration;
import me.alen_alex.tickettool.jda.DiscordInstance;
import org.apache.commons.lang3.Validate;

import javax.security.auth.login.LoginException;
import java.io.File;

public class TicketTool {

    private static String jarPath;
    private static File dataFolder;
    private static Validate validator;
    private static DiscordInstance discordInstance;
    private static SQLDatabase sqlDatabase;
    private static DatabaseManager DatabaseManager;

    public static void main(String[] args) throws Exception {
        jarPath = new File("").getAbsolutePath();
        if(jarPath == null){
            throw new Exception("Invalid Jar Path");
        }
        dataFolder = new File(jarPath);

        initiateBotStartup();
    }

    private static void initiateBotStartup() throws LoginException, InterruptedException {
        Configuration.createConfiguration();
        MessageConfiguration.createConfiguration();
        sqlDatabase = new SQLDatabase();
        Validation.validateStartupParameters();
        discordInstance = new DiscordInstance();
        DatabaseManager = new DatabaseManager(sqlDatabase.initMySQL());
        DatabaseManager.createTables();
        TicketManager.fetchEveryTickets();
    }


    public static String getJarPath() {
        return jarPath;
    }

    public static File getDataFolder() {
        return dataFolder;
    }


    public static Validate getValidator() {
        return validator;
    }

    public static DiscordInstance getDiscordInstance() {
        return discordInstance;
    }

    public static me.alen_alex.tickettool.data.DatabaseManager getDatabaseManager() {
        return DatabaseManager;
    }
}
