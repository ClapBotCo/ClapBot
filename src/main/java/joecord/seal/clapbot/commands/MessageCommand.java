package joecord.seal.clapbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MessageCommand extends MessageReceivedEvent implements
    GenericCommand {

    public MessageCommand(MessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());
    }

    @Override
    public void execute() {
        execute(new String[] {});
    }
}