package joecord.seal.clapbot.commands.conditional_commands;

import java.util.regex.Pattern;

import joecord.seal.clapbot.commands.ConditionalCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BanShadow extends ConditionalCommand {

    public BanShadow(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void execute() {
        double num = Math.random();
        String msg = ((num < 0.5) ? "#ModShadow" : "#BanShadow");
        channel.sendMessage(msg).queue();
    }

    @Override
    public boolean check() {
        return Pattern.matches(
            ".*shadow.*", 
            getMessage().getContentRaw().toLowerCase());
    }

    @Override
    public String getName() {
        return "Ban Shadow";
    }

    @Override
    public String getDescription() {
        return "Responds to 'shadow' with either '#ModShadow' or '#BanShadow'";
    }
}
