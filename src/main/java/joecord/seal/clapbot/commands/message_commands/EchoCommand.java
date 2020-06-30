package joecord.seal.clapbot.commands.message_commands;

import joecord.seal.clapbot.commands.MessageCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends MessageCommand {

    public static final String NAME = "echo";
    public static final String DESCRIPTION = 
        "Responds with the message provided to it";
    public static final String[] ALIASES = new String[] {"say"};
    public static final String USAGE = "echo <message to echo>";

    public EchoCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void execute(String[] arguments) {
        channel.sendMessage(String.join(" ", arguments)).queue();
    }
}
