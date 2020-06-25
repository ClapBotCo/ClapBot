package JoeCord.Seal.ClapBot;

import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Arrays;

public class CommandHandler {
    CommandHandler(MessageChannel channel, String[] message) {
        message[0].toLowerCase();
        try {
            if (message[1].equalsIgnoreCase("echobutnotecho")) {
                System.out.println("clap");
                channel.sendMessage(Arrays.toString(message)).queue();
            }
        }
        catch(Exception ex) {
            System.out.println("Thing");
            }
        }
    }
