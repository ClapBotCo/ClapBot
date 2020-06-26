package joecord.seal.clapbot;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class NotACult {
    NotACult(MessageChannel channel, User Author) {
        channel.sendMessage(":joeL: " + Author.toString() + " has said a forbidden word");
    }
}
