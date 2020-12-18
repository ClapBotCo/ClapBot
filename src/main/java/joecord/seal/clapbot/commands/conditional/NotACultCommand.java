package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import joecord.seal.clapbot.api.legacy.LegacyCommandProperty;
import joecord.seal.clapbot.api.legacy.LegacyGenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NotACultCommand extends LegacyGenericCommand<MessageReceivedEvent> {

    public NotACultCommand() {
        super(MessageReceivedEvent.class, LegacyCommandProperty.CONDITIONAL);

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
