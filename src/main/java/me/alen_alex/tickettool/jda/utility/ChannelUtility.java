package me.alen_alex.tickettool.jda.utility;

import me.alen_alex.tickettool.TicketTool;
import me.alen_alex.tickettool.configuration.Configuration;
import me.alen_alex.tickettool.logger.Logger;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;
import java.util.UUID;

public class ChannelUtility {

    public static TextChannel getChannel(long channelID){
        TextChannel channel = null;
        channel = TicketTool.getDiscordInstance().getDiscordBot().getTextChannelById(channelID);
        return channel;
    }

    public static void sendMessage(TextChannel channel,String message){
        if(message.isEmpty())
            return;
        channel.sendMessage(message).queue();
    }

    public static void sendMessages(TextChannel channel, List<String> messages){
        if(messages.isEmpty())
            return;
        messages.forEach((s)->{
            channel.sendMessage(s).queue();
        });
    }

    public static void createNewChannel(long userID){
        User ticketUser = UserUtility.fetchUser(userID);
        UUID uuid = UUID.randomUUID();
        TextChannel channel = TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID()).createTextChannel(ticketUser.getName()+"",TicketTool.getDiscordInstance().getDiscordBot().getCategoryById(Configuration.getCatogeryID())).complete();
        System.out.println(channel.getId());
        System.out.println("Channel UUID = "+uuid);
    }

    public static void deleteChannel(long userID){
        TextChannel channel = TicketTool.getDiscordInstance().getDiscordBot().getTextChannelById(userID);
        if(channel == null)
            return;

        channel.delete().reason("Failed to create new Ticket").complete();
        Logger.logFatal("Failed to create a new ticket and delete the created channel!");
    }

}
