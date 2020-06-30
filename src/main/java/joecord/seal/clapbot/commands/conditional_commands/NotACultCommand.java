package joecord.seal.clapbot.commands.conditional_commands;

import java.util.regex.Pattern;

import joecord.seal.clapbot.commands.ConditionalCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NotACultCommand extends ConditionalCommand {

    public static final String NAME = "Not a cult";
    public static final String DESCRIPTION = 
        "Notifys anyone that mentions 'cult' that that word is " + 
        "forbidden here";

    public NotACultCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void execute() {
        channel.sendMessage(":joeL: " + getMember().getEffectiveName() + 
            " has said a forbidden word").queue();
    }

    @Override
    public boolean check() {
        return Pattern.matches(
            ".*cult.*", 
            getMessage().getContentRaw().toLowerCase());
    }
}
