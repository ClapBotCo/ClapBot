package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import joecord.seal.clapbot.commands.ConditionalCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BanShadow extends ConditionalCommand {

    public BanShadow() {
        super(
            "Ban Shadow",
            "Responds to 'shadow' with either '#ModShadow' or '#BanShadow'"
        );
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        double num = Math.random();
        String msg = ((num < 0.5) ? "#ModShadow" : "#BanShadow");
        event.getChannel().sendMessage(msg).queue();
    }

    @Override
    public boolean check(MessageReceivedEvent event) {
        return Pattern.matches(
            ".*shadow.*", 
            event.getMessage().getContentRaw().toLowerCase());
    }
}
