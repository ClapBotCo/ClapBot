package joecord.seal.clapbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class ConditionalCommand extends MessageReceivedEvent implements 
    GenericCommand {
    
    public ConditionalCommand(MessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());
    }

    @Override
    public void execute(String[] arguments) {
        execute();
    }

    public abstract boolean check();
}