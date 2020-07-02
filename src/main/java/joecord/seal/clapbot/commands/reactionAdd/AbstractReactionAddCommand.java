package joecord.seal.clapbot.commands.reactionAdd;

import joecord.seal.clapbot.commands.GenericCommand;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public abstract class AbstractReactionAddCommand implements
    GenericCommand<GuildMessageReactionAddEvent> {
    
    /** The name of the command */
    protected String name = "";
    /** The description string of the command, detailing what it does */
    protected String description = "";

    @Override
    public void execute(GuildMessageReactionAddEvent event, String[] arguments) {
        execute(event);
    }
    
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
        AbstractReactionAddCommand other;

        if(obj instanceof AbstractReactionAddCommand) {
            other = (AbstractReactionAddCommand)obj;

            if(other.getName().equals(this.getName()) &&
                other.getDescription().equals(this.getDescription())) {
                    
                equal = true;
            }
        }

        return equal;
    }
}