package joecord.seal.clapbot.commands.message;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends AbstractMessageCommand {

    public EchoCommand() {
        this.name = "echo";
        this.description = "Responds with the message provided to it";
        this.usage = "echo <message to echo>";
        this.aliases = new String[] {"say"};
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] arguments) {
        event.getChannel().sendMessage(String.join(" ", arguments)).queue();
    }
}
