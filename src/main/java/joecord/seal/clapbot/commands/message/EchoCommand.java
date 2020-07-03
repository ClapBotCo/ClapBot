package joecord.seal.clapbot.commands.message;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends GenericCommand<MessageReceivedEvent> {

    public EchoCommand() {
        super(CommandProperty.INVOKED, CommandProperty.USES_ARGUMENTS);

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
