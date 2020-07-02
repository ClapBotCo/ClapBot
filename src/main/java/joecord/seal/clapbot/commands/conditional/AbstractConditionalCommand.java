package joecord.seal.clapbot.commands.conditional;

import joecord.seal.clapbot.commands.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractConditionalCommand
    implements GenericCommand<MessageReceivedEvent> {

    /** The name of the command */
    protected String name = "";
    /** The description string of the command, detailing what it does */
    protected String description = "";

    @Override
    public void execute(MessageReceivedEvent event, String[] arguments) {
        execute(event);
    }

    public abstract boolean check(MessageReceivedEvent event);

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        AbstractConditionalCommand other;

        if(obj instanceof AbstractConditionalCommand) {
            other = (AbstractConditionalCommand)obj;

            if(other.getName().equals(this.getName()) &&
                other.getDescription().equals(this.getDescription())) {
                    
                equal = true;
            }
        }

        return equal;
    }
}