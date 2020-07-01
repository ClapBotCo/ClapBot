package joecord.seal.clapbot.commands.message;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends AbstractMessageCommand {

    public EchoCommand() {
        super(
            "echo", // Name
            "Responds with the message provided to it", // Description
            "echo <message to echo>", // Usage
            new String[] {"say"} // Aliases
        );
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] arguments) {
        event.getChannel().sendMessage(String.join(" ", arguments)).queue();
    }
}
