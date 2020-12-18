package joecord.seal.clapbot.commands.conditional;

import java.io.PrintStream;

import joecord.seal.clapbot.api.legacy.LegacyCommandProperty;
import joecord.seal.clapbot.api.legacy.LegacyGenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageLoggerCommand extends LegacyGenericCommand<MessageReceivedEvent> {
    private PrintStream out;

    public MessageLoggerCommand(PrintStream out) {
        super(MessageReceivedEvent.class, LegacyCommandProperty.RESPECT_BOTS);

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