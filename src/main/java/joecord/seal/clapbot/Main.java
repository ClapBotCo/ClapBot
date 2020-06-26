package joecord.seal.clapbot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.security.auth.login.LoginException;
import java.util.EventListener;

public class Main{
    public static void main(String[] args) throws LoginException {
        String token = System.getenv("DISCORD_CLAPBOT_TOKEN");

        JDA api = JDABuilder.createDefault(token)
                .setActivity(Activity.playing("TTT"))
                .setStatus(OnlineStatus.IDLE)
                .addEventListeners(new MessageListener())
                .build();
    }
}
