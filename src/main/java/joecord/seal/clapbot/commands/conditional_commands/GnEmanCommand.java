package joecord.seal.clapbot.commands.conditional_commands;

import java.util.regex.Pattern;

import joecord.seal.clapbot.commands.ConditionalCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GnEmanCommand extends ConditionalCommand {

    public static final String NAME = "gn eman";
    public static final String DESCRIPTION = 
        "Says gn to anyone that says 'gn modmail' :)";

    public GnEmanCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void execute() {
        channel.sendMessage("gn " + getMember().getEffectiveName()
            .toLowerCase().replaceAll("quirky", "")).queue();
    }

    @Override
    public boolean check() {
        return Pattern.matches(".*gn modmail.*", getMessage().getContentRaw());
     
        /* Alternative that only responds to eman
        return (
            Pattern.matches(".*gn modmail.*", getMessage().getContentRaw())
            &&
            getMember.getId().equals("280177678282129409"))
        };
        */
    }
}
