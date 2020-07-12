package joecord.seal.clapbot.commands.conditional;

import java.io.PrintStream;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageLoggerCommand extends GenericCommand<MessageReceivedEvent> {
    private PrintStream out;

    public MessageLoggerCommand(PrintStream out) {
        super(MessageReceivedEvent.class, CommandProperty.RESPECT_BOTS);

        this.displayName = "Logging";
        this.description = "Logs MessageReceivedEvent details to System.out";

        this.out = out;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        this.out.println(String.format(
            "#%s [%s]: %s",
            event.getTextChannel().getName(),
            event.getAuthor().getName(),
            event.getMessage().getContentRaw())
        );
    }
}