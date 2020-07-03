package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NotACultCommand extends GenericCommand<MessageReceivedEvent> {

    public NotACultCommand() {
        super(CommandProperty.CONDITIONAL);

        this.displayName = "Not a cult";
        this.description = 
            "Reminds people that 'cult' is a forbidden word here";

        super.setConditionDesc("Message must contain 'cult'");
        super.setCondition(event -> Pattern.matches(
                ".*cult.*", 
                event.getMessage().getContentRaw().toLowerCase()));
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(
            ":joeL: " + event.getMember().getEffectiveName() + 
                " has said a forbidden word")
            .queue();
    }
}
