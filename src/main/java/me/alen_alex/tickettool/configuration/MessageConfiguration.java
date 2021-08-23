package me.alen_alex.tickettool.configuration;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import me.alen_alex.tickettool.TicketTool;
import me.alen_alex.tickettool.logger.Logger;

import java.io.InputStream;

public class MessageConfiguration {

    private static Yaml messageConfig;

    public static void createConfiguration() {
        if (TicketTool.getDataFolder().exists()) {
            TicketTool.getDataFolder().mkdirs();
        }
        messageConfig = new Yaml("message.yml", TicketTool.getJarPath(), getInputStream());
        messageConfig.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);
        messageConfig.getOrSetDefault("version", "1.0");
        Logger.logNormal("Succesfully created/found messages. Starting to load configuration");
    }

    private static InputStream getInputStream() {
        ClassLoader classLoader = Configuration.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("messages.yml");
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + "messages.yml");
        } else {
            return inputStream;
        }
    }


}
