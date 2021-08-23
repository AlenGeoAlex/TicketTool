package me.alen_alex.tickettool.jda;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.alen_alex.tickettool.configuration.Configuration;

import me.alen_alex.tickettool.jda.commands.NewCommand;
import me.alen_alex.tickettool.logger.Logger;
import me.alen_alex.tickettool.ticket.TicketManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordInstance {

    private final JDA discordBot;
    public DiscordInstance() throws LoginException, InterruptedException {
        EventWaiter waiter = new EventWaiter();
        CommandClientBuilder client = new CommandClientBuilder();
        client.setPrefix(Configuration.getPrefix());
        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        client.setHelpWord("help");
        client.setOwnerId("403569609518743552");
        client.addCommand(new NewCommand());
        //this.discordBot = JDABuilder.createLight(Configuration.getBotToken(), GatewayIntent.GUILD_MEMBERS).build();
        JDABuilder builder = JDABuilder.createDefault(Configuration.getBotToken());
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES,GatewayIntent.GUILD_MEMBERS,GatewayIntent.DIRECT_MESSAGES);
        builder.enableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        this.discordBot = builder.build();
        this.discordBot.awaitReady();
        this.discordBot.addEventListener(waiter, client.build());

        Logger.logNormal("Connected to the Discord API");
        Logger.logNormal("Starting to fetch tickets");
    }

    public JDA getDiscordBot() {
        return discordBot;
    }

    public void disconnect(){
        this.discordBot.shutdown();
    }
}
