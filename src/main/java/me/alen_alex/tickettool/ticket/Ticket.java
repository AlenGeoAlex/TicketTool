package me.alen_alex.tickettool.ticket;

import me.alen_alex.tickettool.jda.utility.ChannelUtility;
import me.alen_alex.tickettool.jda.utility.UserUtility;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ticket {

    private long userID;
    private User ticketAuthor,ticketClaimed;
    private TextChannel ticketChannel;
    private UUID ticketUUID;
    private List<User> ticketUsers;
    private boolean claimed;
    private long ticketClaimedBy;
    private final List<String> NOUSERFOUND = new ArrayList<String>(){{
        add("**__Startup Alert__**\n");
        add("**The author of the the ticket (*"+userID+"*) seems to be not in discord anymore!, If you believe the author is still here..Please avoid this message**");
        add("*Ticket Tool*");
    }};

    public Ticket(@NotNull TextChannel channel,@NotNull long userID,@NotNull UUID ticketUUID,@NotNull boolean claimed, long ticketClaimedBy, List<Long> usersInTicket) {
        this.ticketChannel = channel;
        this.userID = userID;
        this.ticketUUID = ticketUUID;
        this.claimed = claimed;
        this.ticketClaimedBy = ticketClaimedBy;
        this.ticketAuthor = UserUtility.fetchUser(userID);
        if(ticketAuthor == null){
            ChannelUtility.sendMessages(ticketChannel,NOUSERFOUND);
        }
        if(claimed){
            if(ticketClaimedBy != 0){
                ticketClaimed = UserUtility.fetchUser(this.ticketClaimedBy);
            }
        }
    }


    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public long getTicketClaimedBy() {
        return ticketClaimedBy;
    }

    public void setTicketClaimedBy(long ticketClaimedBy) {
        this.ticketClaimedBy = ticketClaimedBy;
    }

    public long getUserID() {
        return userID;
    }

    public User getTicketAuthor() {
        return ticketAuthor;
    }

    public User getTicketClaimed() {
        return ticketClaimed;
    }

    public TextChannel getTicketChannel() {
        return ticketChannel;
    }

    public UUID getTicketUUID() {
        return ticketUUID;
    }

    public List<User> getTicketUsers() {
        return ticketUsers;
    }

}
