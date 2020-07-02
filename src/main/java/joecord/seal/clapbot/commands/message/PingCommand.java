package joecord.seal.clapbot.commands.message;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends AbstractMessageCommand {
    
    public PingCommand() {
        this.name = "ping";
        this.description = "Gets the bot's response time in milliseconds";
        this.usage = this.name;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] arguments) {
        long initialTime = System.currentTimeMillis();

        event.getChannel().sendMessage("⏱")
            .queue(response ->
                response.editMessageFormat(
                    "Pong! %d ms", System.currentTimeMillis() - initialTime)
                    .queue());
    }
}