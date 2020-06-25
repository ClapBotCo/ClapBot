package JoeCord.Seal.ClapBot;

import net.dv8tion.jda.api.entities.MessageChannel;

public class CommandHandler {
    CommandHandler(MessageChannel channel, String[] message) {
        message[0].toLowerCase();
        if (message[0].contains("clap")) {
            System.out.println("clap");
        }
    }
}
