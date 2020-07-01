package joecord.seal.clapbot;

import joecord.seal.clapbot.commands.conditional.*;
import joecord.seal.clapbot.commands.memberJoin.JoinMessageCommand;
import joecord.seal.clapbot.commands.message.*;
import joecord.seal.clapbot.commands.reactionAdd.AddRoleOnReactionAddCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

public class ClapBot {

    private static ClapBot instance;

    public static ClapBot getInstance() {
        return instance;
    }

    private CommandHandler commandHandler;
    private String token;
    private JDA api;

    public ClapBot(String token){
        instance = this;
        this.token = token;
        System.out.println(token);
        //JoeCord :joeclap: is <:joeclap:551531713487175682>
        this.commandHandler = new CommandHandler("clap ");

        // Register commands
        this.commandHandler.register(new EchoCommand());

        this.commandHandler.register(new BanShadowCommand());
        this.commandHandler.register(new GnEmanCommand());
        this.commandHandler.register(new NotACultCommand());
        this.commandHandler.register(new NerdDetectedCommand());

        this.commandHandler.register(new JoinMessageCommand("727104182787506187"));
            // Channel ID of #spam-claps in Bits and Bots
        
        this.commandHandler.register(new AddRoleOnReactionAddCommand(
            "727852002448637972", "joeclap", "724184550548308008"));
            /* Currently set to a random message in #spam-claps and to the
             * 'bots' role in Bits and Bots */
    
        try {
            this.api = buildAPI();
        } catch (LoginException e) {
            System.out.println("Error creating bot API");
            System.out.println(e);
            System.exit(0);
        }
    }

    public ClapBot(){
        this(System.getenv("DISCORD_CLAPBOT_TOKEN"));
    }

    public JDA buildAPI() throws LoginException {
        List<GatewayIntent> intents = Arrays.asList(
            GatewayIntent.GUILD_MESSAGES, // For MessageReceivedEvent
            GatewayIntent.GUILD_MEMBERS, // For GuildMemberJoinEvent
            GatewayIntent.GUILD_MESSAGE_REACTIONS // For reactions
        );
        List<CacheFlag> disabledCaches = Arrays.asList(
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.VOICE_STATE, 
            CacheFlag.EMOTE
        );

        return JDABuilder.create(this.token, intents)
                .setActivity(Activity.playing("TTT"))
                .setStatus(OnlineStatus.IDLE)
                .addEventListeners(this.commandHandler)
                .disableCache(disabledCaches)
                .build();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public JDA getApi() {
        return api;
    }
}
