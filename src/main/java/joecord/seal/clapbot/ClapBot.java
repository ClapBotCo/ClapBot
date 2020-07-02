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

        // Register message commands
        this.commandHandler.register(new EchoCommand());
        this.commandHandler.register(new PingCommand());

        // Register conditional commands
        this.commandHandler.register(new BanShadowCommand());
        this.commandHandler.register(new GnEmanCommand());
        this.commandHandler.register(new NotACultCommand());
        this.commandHandler.register(new NerdDetectedCommand());

        // Register member join commands
        this.commandHandler.register(new JoinMessageCommand("727104182787506187"));
            // Channel ID of #spam-claps in Bits and Bots
        
        // Register reaction add commands
        this.commandHandler.register(new AddRoleOnReactionAddCommand(
            "728136956646522920", // Some random message ID in #spam-claps
            "U+1f973", // :partying_face:
            "728136332202999849" /* The @PeasantClaps role ID */));
    
        try {
            this.api = buildAPI();
        } catch (LoginException | InterruptedException e) {
            System.out.println("Error creating bot API");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public ClapBot(){
        this(System.getenv("DISCORD_CLAPBOT_TOKEN"));
    }

    public JDA buildAPI() throws LoginException, InterruptedException {
        JDA api;
        List<CacheFlag> disabledCaches = Arrays.asList(
            CacheFlag.ACTIVITY,
            CacheFlag.CLIENT_STATUS,
            CacheFlag.VOICE_STATE, 
            CacheFlag.EMOTE
        );

        api = JDABuilder.create(this.token, commandHandler.getIntents())
            .setActivity(Activity.playing("Starting up..."))
            .setStatus(OnlineStatus.DO_NOT_DISTURB)
            .addEventListeners(this.commandHandler)
            .disableCache(disabledCaches)
            .build();

        // Wait for the bot to finish starting up
        api.awaitReady();

        api.getPresence()
            .setPresence(OnlineStatus.ONLINE, Activity.playing("TTT"));

        return api;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public JDA getApi() {
        return api;
    }
}
