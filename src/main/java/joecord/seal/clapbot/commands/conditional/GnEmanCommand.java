package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GnEmanCommand extends AbstractConditionalCommand {

    public GnEmanCommand() {
        super(
            "gn eman",
            "Says gn to anyone that says 'gn modmail' :)"
        );
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(
            "gn " + event.getMember().getEffectiveName().toLowerCase()
                .replaceAll("quirky", ""))
        .queue();
    }

    @Override
    public boolean check(MessageReceivedEvent event) {
        return Pattern.matches(
            ".*gn modmail.*",
            event.getMessage().getContentRaw());
     
        /* Alternative that only responds to eman
        return (
            Pattern.matches(".*gn modmail.*", getMessage().getContentRaw())
            &&
            getMember.getId().equals("280177678282129409"))
        };
        */
    }
}
