package me.alen_alex.tickettool.jda.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.alen_alex.tickettool.ticket.TicketManager;

public class NewCommand extends Command {

    public NewCommand(){
        this.name = "new";
        this.aliases = new String[]{"create","ticket","supportticket","support"};
        this.help = "Create a new support ticket!";
        this.cooldown = 20;
        this.cooldownScope = CooldownScope.GLOBAL;
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        if(commandEvent.getAuthor().isBot())
            return;

        if(TicketManager.hasTicketLimitReached(commandEvent.getAuthor().getIdLong())) {
            return;
        }

        TicketManager.createNewTicket(commandEvent.getAuthor().getIdLong());

    }
}
