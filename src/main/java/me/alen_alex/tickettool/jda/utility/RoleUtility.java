package me.alen_alex.tickettool.jda.utility;

import me.alen_alex.tickettool.TicketTool;
import me.alen_alex.tickettool.configuration.Configuration;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class RoleUtility {

    public static Role getRole(long roleID){
        return TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID()).getRoleById(roleID);
    }

    public static Role getRole(String roleName){
        return TicketTool.getDiscordInstance().getDiscordBot().getGuildById(Configuration.getGuildID()).getRolesByName(roleName,false).get(0);
    }

}
