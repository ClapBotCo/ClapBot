package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GnEmanCommand extends GenericCommand<MessageReceivedEvent> {

    public GnEmanCommand() {
        super(MessageReceivedEvent.class, CommandProperty.CONDITIONAL);

        this.displayName = "gn eman";
        this.description = "Says gn to anyone that says 'gn modmail' :)";

        super.setConditionDesc("Message must contain 'gn modmail'");
        super.setCondition(event -> {
            return Pattern.matches(
            ".*gn modmail.*",
            event.getMessage().getContentRaw());
        });
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(
            "gn " + event.getMember().getEffectiveName().toLowerCase()
                .replaceAll("quirky", ""))
        .queue();
    }
}
