package joecord.seal.clapbot.commands.conditional;

import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LoggingCommand extends GenericCommand<MessageReceivedEvent> {
    public LoggingCommand() {
        super(MessageReceivedEvent.class);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        System.out.println(String.format(
            "#%s [%s]: %s\n",
            event.getTextChannel().getName(),
            event.getAuthor().getName(),
            event.getMessage().getContentRaw())
        );
    }
}