package joecord.seal.clapbot.commands.message_commands;

import joecord.seal.clapbot.commands.MessageCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends MessageCommand {

    public static final String name = "echo";

    public EchoCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void execute(String[] arguments) {
        channel.sendMessage(String.join(" ", arguments)).queue();
    }

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Responds with the message provided to it";
    }

    @Override
    public String getUsage() {
        return "echo <message to echo>";
    }
}
