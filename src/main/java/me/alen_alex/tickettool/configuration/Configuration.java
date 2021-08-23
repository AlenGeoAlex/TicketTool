package me.alen_alex.tickettool.configuration;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import me.alen_alex.tickettool.TicketTool;
import me.alen_alex.tickettool.logger.Logger;

import java.io.InputStream;
import java.util.List;

public class Configuration {

    private static Yaml config;
    //-----------------------------------------------------
    private static String botToken;
    private static String prefix, botStatus;
    private static String sqlHost,sqlPort,sqlUsername,sqlPassword,sqlDatabase;
    private static long guildID,catogeryID;
    private static int ticketLimit;
    private static List<String> staffRoles;

    //-----------------------------------------------------
    public static void createConfiguration() {
        if (TicketTool.getDataFolder().exists()) {
            TicketTool.getDataFolder().mkdirs();
        }
        config = new Yaml("config.yml", TicketTool.getJarPath(), getInputStream());
        config.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
        config.getOrSetDefault("version", "1.0");
        Logger.logNormal("Succesfully created/found config. Starting to load configuration");
        loadConfiguration();
    }

    private static InputStream getInputStream() {
        ClassLoader classLoader = Configuration.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("config.yml");
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + "config.yml");
        } else {
            return inputStream;
        }
    }

    public static void loadConfiguration() {
        botToken = config.getString("bot.token");
        prefix = config.getString("bot.prefix");
        botStatus = config.getString("bot.status");
        sqlHost = config.getString("storage.host");
        sqlPort = config.getString("storage.port");
        sqlUsername = config.getString("storage.username");
        sqlPassword = config.getString("storage.password");
        sqlDatabase = config.getString("storage.databasename");
        guildID = config.getLong("bot.guild-id");
        catogeryID = config.getLong("bot.catogery-id");
        ticketLimit = config.getInt("settings.no-of-tickets-a-user-can-open");
        staffRoles = config.getStringList("settings.staff-roles");
        Logger.logNormal("Configuration Loaded Completed!");
    }

    public static String getBotToken() {
        return botToken;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getBotStatus() {
        return botStatus;
    }

    public static String getSqlHost() {
        return sqlHost;
    }

    public static String getSqlPort() {
        return sqlPort;
    }

    public static String getSqlUsername() {
        return sqlUsername;
    }

    public static String getSqlPassword() {
        return sqlPassword;
    }

    public static String getSqlDatabase() {
        return sqlDatabase;
    }

    public static long getGuildID() {
        return guildID;
    }

    public static long getCatogeryID() {
        return catogeryID;
    }

    public static int getTicketLimit() {
        return ticketLimit;
    }

    public static List<String> getStaffRoles() {
        return staffRoles;
    }

    public static Yaml getConfig() {
        return config;
    }
}