package me.alen_alex.tickettool.jda.utility;

import me.alen_alex.tickettool.TicketTool;
import me.alen_alex.tickettool.configuration.Configuration;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class UserUtility {

    public static User fetchUser(long userID){
        User user = null;
        user = TicketTool.getDiscordInstance().getDiscordBot().getUserById(userID);
        return user;
    }

    public static Member fetchMember(long userID){
        return TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID()).getMemberById(userID);
    }

}
