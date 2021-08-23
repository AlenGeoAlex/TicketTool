package me.alen_alex.tickettool.ticket;

import me.alen_alex.tickettool.TicketTool;
import me.alen_alex.tickettool.configuration.Configuration;
import me.alen_alex.tickettool.data.DatabaseManager;
import me.alen_alex.tickettool.jda.utility.ChannelUtility;
import me.alen_alex.tickettool.jda.utility.RoleUtility;
import me.alen_alex.tickettool.jda.utility.UserUtility;
import me.alen_alex.tickettool.logger.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TicketManager {

    private static HashMap<Long, Ticket> openedTickets = new HashMap<Long, Ticket>();


    public static void fetchEveryTickets(){
        List<Long> deletionQueue = new ArrayList<Long>();
        ResultSet set = TicketTool.getDatabaseManager().getTickets();
        try {
            while (set.next()) {
                if(ChannelUtility.getChannel(set.getLong("channelid")) != null) {
                    openedTickets.put(set.getLong("channelid"), new Ticket(ChannelUtility.getChannel(set.getLong("channelid")), set.getLong("userid"), UUID.fromString(set.getString("uuid")), set.getBoolean("claimed"), set.getLong("claimedBy"), userListParser(set.getString("users"))));
                    Logger.logNormal("Succesfully Cached Channel - "+set.getLong("channelid")+" of user "+set.getLong("userid")+" with the UUID - "+UUID.fromString(set.getString("uuid")));
                }
                else{
                    Logger.logError("The channel with ID "+set.getLong("channelid")+" is missing!");
                    Logger.logWarn("Added to deletion queue!");
                    deletionQueue.add(set.getLong("channelid"));
                }

            }
            set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Logger.logNormal("Starting to clear garbage tickets from database");
        if(!deletionQueue.isEmpty()){
            CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    deletionQueue.forEach((chnID) -> {
                        TicketTool.getDatabaseManager().deleteATicket(Long.valueOf(chnID));
                        Logger.logNormal(" --> Cleared ticket with the id "+chnID);
                    });
                    deletionQueue.clear();
                    Logger.logNormal("Completing Async Garbage Remover!");
                }
            });
        }
    }

    private static List<Long> userListParser(String users){
        List<Long> userList = new ArrayList<Long>();
        if(users != null) {
            String[] user = users.split("/");
            for (String userID : user) {
                userList.add(Long.valueOf(userID));
            }
        }
        return userList;
    }


    public static void createNewTicket(long userID){
        if(hasTicketLimitReached(userID)){
            //TODO MESSAGE FOR TICKET LIMIT REACHED
        }
        Category supportCategory = TicketTool.getDiscordInstance().getDiscordBot().getCategoryById(Configuration.getCatogeryID());
        if(supportCategory == null){
            Logger.logFatal("Category mentioned doesn't seems to exist! Aborting creation");
            return;
        }
        User user = UserUtility.fetchUser(userID);
        if(user == null) {
            TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID()).retrieveMemberById(userID,true).queue();
            user = UserUtility.fetchUser(userID);
        }
        try {
            TextChannel newChannel = Objects.requireNonNull(TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID())).createTextChannel("support-"+ user.getName(),supportCategory).complete();
            if(TicketTool.getDatabaseManager().registerNewTicket(userID, newChannel.getIdLong(),UUID.randomUUID())){
                //TODO PING MEMBERS
                //TODO SEND FIRST MESSAGE
            /*
            Adding permission to the user
             */
                if(newChannel.getPermissionOverride(UserUtility.fetchMember(userID)) == null) {
                    newChannel.createPermissionOverride(UserUtility.fetchMember(userID) ).setAllow(
                                    Permission.MESSAGE_WRITE,
                                    Permission.MESSAGE_READ,
                                    Permission.MESSAGE_ATTACH_FILES,
                                    Permission.MESSAGE_HISTORY,
                                    Permission.MESSAGE_EMBED_LINKS,
                                    Permission.MESSAGE_ADD_REACTION,
                                    Permission.MESSAGE_EXT_EMOJI
                            ).setDeny(
                                    Permission.MANAGE_THREADS,
                                    Permission.USE_PUBLIC_THREADS,
                                    Permission.USE_PRIVATE_THREADS,
                                    Permission.MANAGE_PERMISSIONS,
                                    Permission.MESSAGE_MENTION_EVERYONE
                            ).reason("Adjusting ticket Author permission")
                            .queue();
                }
            /*
            Adding permission to staff roles
             */
                if(!Configuration.getStaffRoles().isEmpty()){
                    Configuration.getStaffRoles().forEach((role) -> {
                        if(newChannel.getPermissionOverride(RoleUtility.getRole(role)) == null){
                            newChannel.createPermissionOverride(RoleUtility.getRole(role)).setAllow(
                                            Permission.MESSAGE_WRITE,
                                            Permission.MESSAGE_READ,
                                            Permission.MESSAGE_ATTACH_FILES,
                                            Permission.MESSAGE_HISTORY,
                                            Permission.MESSAGE_EMBED_LINKS,
                                            Permission.MESSAGE_ADD_REACTION,
                                            Permission.MESSAGE_EXT_EMOJI
                                    ).setDeny(
                                            Permission.MANAGE_THREADS,
                                            Permission.USE_PUBLIC_THREADS,
                                            Permission.USE_PRIVATE_THREADS,
                                            Permission.MANAGE_PERMISSIONS,
                                            Permission.MESSAGE_MENTION_EVERYONE
                                    ).reason("Adjusting ticket Staff permission")
                                    .queue();
                        }
                    });
                }

                newChannel.createPermissionOverride(TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID()).getPublicRole()).setDeny(
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_READ,
                        Permission.MESSAGE_ATTACH_FILES,
                        Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_EMBED_LINKS,
                        Permission.MESSAGE_ADD_REACTION,
                        Permission.MESSAGE_EXT_EMOJI
                ).reason("Removing Public Role From Tickets!").queue();
            }else{
                ChannelUtility.deleteChannel(newChannel.getIdLong());
            }
        }catch (NullPointerException e){
            //TODO EMBED ERROR OCCURED
            Logger.logFatal("Unable to create ticket for user "+userID+" as NullPointerException has occurred");
        }

    }

    public static boolean hasTicketLimitReached(long userID){
        return (TicketTool.getDatabaseManager().getUserTicketCount(userID) >= Configuration.getTicketLimit());
    }


}
