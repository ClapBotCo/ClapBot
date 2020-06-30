package joecord.seal.clapbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class ConditionalCommand
    implements GenericCommand<MessageReceivedEvent> {

    protected String name;
    protected String description;

    /**
     * Construct a new conditional command.
     * @param name The name of the command
     * @param description The description string of the command
     */
    public ConditionalCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
}