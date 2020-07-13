package joecord.seal.clapbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import joecord.seal.clapbot.commands.conditional.*;
import joecord.seal.clapbot.commands.memberJoin.*;
import joecord.seal.clapbot.commands.message.*;
import joecord.seal.clapbot.commands.reactionAdd.*;

public class ClapBot {

    private static ClapBot instance;

    public static ClapBot getInstance() {
        return instance;
    }

    private CommandHandler commandHandler;
    private String token;
    private JDA api;    

    public ClapBot() {
        this(System.getenv("DISCORD_CLAPBOT_TOKEN"));
    }

    public ClapBot(String token) {
        instance = this;
        this.token = token;
        this.commandHandler = buildCommandHandler("clap ");
        this.api = buildAPI();
    }

    private CommandHandler buildCommandHandler(String prefix) {
        CommandHandler handler = new CommandHandler(prefix);

        // Register message commands
        handler.register(new PingCommand());
        handler.register(new EchoCommand());

        // Register conditional commands
        handler.register(new BanShadowCommand());
        handler.register(new MessageLoggerCommand(System.out));
        handler.register(new GnEmanCommand());
        handler.register(new NotACultCommand());
        handler.register(new NerdDetectedCommand());

        // Register member join commands
        handler.register(new JoinMessageCommand("727104182787506187"));
            // Channel ID of #spam-claps in Bits and Bots
        
        // Register reaction add commands
        handler.register(new AddRoleOnReactionAddCommand(
            "728136956646522920", // Some random message ID in #spam-claps
            "U+1f973", // :partying_face:
            "728136332202999849")); // The @PeasantClaps role ID
        
        return handler;
    }

    private JDA buildAPI() {
        JDABuilder builder;
        JDA api = null;
        List<CacheFlag> disabledCaches = Arrays.asList(
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.VOICE_STATE, 
            CacheFlag.EMOTE
        );

        builder = JDABuilder.create(this.token, commandHandler.getRequiredIntents())
            .setActivity(Activity.playing("Starting up..."))
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .addEventListeners(new Listener(this.commandHandler))
            .disableCache(disabledCaches);

        try {
            api = builder.build();
        }
        catch(LoginException e) {
            System.out.println("[ClapBot Error] Error logging in to discord:");
            e.printStackTrace();
            System.exit(1);
        }

        try{
            // Wait for the bot to finish starting up
            api.awaitReady();
        }
        catch(InterruptedException e) {
            System.out.println("[ClapBot Error] JDA was interrupted while " + 
                "starting up:");
            e.printStackTrace();
            System.exit(1);
        }

        api.getPresence().setPresence(
            OnlineStatus.ONLINE, Activity.playing("TTT"));

        return api;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public JDA getApi() {
        return api;
    }
}
