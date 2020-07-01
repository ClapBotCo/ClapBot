package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NotACultCommand extends AbstractConditionalCommand {

    public NotACultCommand() {
        this.name = "Not a cult";
        this.description = 
            "Reminds people that 'cult' is a forbidden word here";
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(
            ":joeL: " + event.getMember().getEffectiveName() + 
                " has said a forbidden word")
            .queue();
    }

    @Override
    public boolean check(MessageReceivedEvent event) {
        return Pattern.matches(
            ".*cult.*", 
            event.getMessage().getContentRaw().toLowerCase());
    }
}
