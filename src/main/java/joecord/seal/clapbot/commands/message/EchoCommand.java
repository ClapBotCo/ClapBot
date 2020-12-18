package joecord.seal.clapbot.commands.message;

import joecord.seal.clapbot.api.legacy.LegacyCommandProperty;
import joecord.seal.clapbot.api.legacy.LegacyGenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends LegacyGenericCommand<MessageReceivedEvent> {

    public EchoCommand() {
        super(MessageReceivedEvent.class,
            LegacyCommandProperty.INVOKED, LegacyCommandProperty.USES_ARGUMENTS);

        this.displayName = "Echo";
        this.description = "Responds with the message provided to it";

        super.setArgumentsDesc("echo <message to echo>");
        super.setAliases("echo", "say");
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(String.join(" ", super.getArguments()))
            .queue();
    }
}
