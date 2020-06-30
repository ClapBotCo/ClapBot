package joecord.seal.clapbot;

import joecord.seal.clapbot.commands.CommandDescriptor;
import joecord.seal.clapbot.commands.conditional_commands.*;
import joecord.seal.clapbot.commands.message_commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

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
        this.commandHandler.registerMessageCommand(
            new CommandDescriptor<EchoCommand>(EchoCommand.class));
        this.commandHandler.registerConditonalCommand(
            new CommandDescriptor<BanShadow>(BanShadow.class));
        this.commandHandler.registerConditonalCommand(
            new CommandDescriptor<GnEmanCommand>(GnEmanCommand.class));
        this.commandHandler.registerConditonalCommand(
            new CommandDescriptor<NotACultCommand>(NotACultCommand.class));
    
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
        return JDABuilder.createDefault(this.token)
                .setActivity(Activity.playing("TTT"))
                .setStatus(OnlineStatus.IDLE)
                .addEventListeners(this.commandHandler)
                .build();
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public JDA getApi() {
        return api;
    }
}
