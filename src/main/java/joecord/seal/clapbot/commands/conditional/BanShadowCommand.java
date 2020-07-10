package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BanShadowCommand extends GenericCommand<MessageReceivedEvent> {

    public BanShadowCommand() {
        super(MessageReceivedEvent.class, CommandProperty.CONDITIONAL);

        this.displayName = "Ban Shadow";
        this.description = 
            "Responds to 'shadow' with either '#ModShadow' or '#BanShadow'";

        super.setConditionDesc("Message must contain 'shadow'");
        super.setCondition(event -> {
            return Pattern.matches(
                ".*shadow.*", 
                event.getMessage().getContentRaw().toLowerCase());
        });
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        double num = Math.random();
        String msg = ((num < 0.5) ? "#ModShadow" : "#BanShadow");
        event.getChannel().sendMessage(msg).queue();
    }
}
