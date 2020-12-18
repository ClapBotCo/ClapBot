package joecord.seal.clapbot.commands.message;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends GenericCommand<MessageReceivedEvent> {
    
    public PingCommand() {
        super(MessageReceivedEvent.class, CommandProperty.INVOKED);

        this.displayName = "Ping";
        this.description = "Gets the bot's response time in milliseconds";
        
        super.setAliases("ping");
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        long initialTime = System.currentTimeMillis();

        event.getChannel().sendMessage("Ping...")
            .queue(response ->
                response.editMessageFormat(
                    "Pong! %d ms", System.currentTimeMillis() - initialTime)
                    .queue());
    }
}