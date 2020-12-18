package joecord.seal.clapbot.commands.message;

import joecord.seal.clapbot.api.legacy.LegacyCommandProperty;
import joecord.seal.clapbot.api.legacy.LegacyGenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends LegacyGenericCommand<MessageReceivedEvent> {
    
    public PingCommand() {
        super(MessageReceivedEvent.class, LegacyCommandProperty.INVOKED);

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