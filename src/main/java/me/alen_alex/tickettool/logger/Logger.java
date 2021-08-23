package me.alen_alex.tickettool.logger;

import me.alen_alex.tickettool.TicketTool;


public class Logger {

    public static void logNormal(String message){
        if(message.isEmpty())
            return;
        System.out.println(ConsoleColors.CYAN+"[LOG] - "+ConsoleColors.RESET+message);
    }

    public static void logWarn(String message){
        if(message.isEmpty())
            return;
        System.out.println(ConsoleColors.YELLOW_BACKGROUND+"[WARN] - "+ConsoleColors.YELLOW+message+ConsoleColors.RESET);
    }

    public static void logError(String message){
        if(message.isEmpty())
            return;
        System.out.println(ConsoleColors.RED_BACKGROUND_BRIGHT+"[ERROR] - "+ConsoleColors.RED+message+ConsoleColors.RED);
    }

    public static void logDebug(String message){
        if(message.isEmpty())
            return;
        System.out.println(ConsoleColors.PURPLE+"[DEBUG] - "+ConsoleColors.RESET+message);
    }

    public static void logFatal(String message){
        if(message.isEmpty())
            return;
        System.out.println(ConsoleColors.RED_BOLD+"[FATAL] - "+message+ConsoleColors.RESET);
    }
}
