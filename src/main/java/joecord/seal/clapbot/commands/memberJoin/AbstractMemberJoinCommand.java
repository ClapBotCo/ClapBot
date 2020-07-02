package joecord.seal.clapbot.commands.memberJoin;

import joecord.seal.clapbot.commands.GenericCommand;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public abstract class AbstractMemberJoinCommand implements 
    GenericCommand<GuildMemberJoinEvent> {

    /** The name of the command */
    protected String name = "";
    /** The description string of the command, detailing what it does */
    protected String description = "";

    @Override
    public void execute(GuildMemberJoinEvent event, String[] arguments) {
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
        AbstractMemberJoinCommand other;

        if(obj instanceof AbstractMemberJoinCommand) {
            other = (AbstractMemberJoinCommand)obj;

            if(other.getName().equals(this.getName()) &&
                other.getDescription().equals(this.getDescription())) {
                    
                equal = true;
            }
        }

        return equal;
    }
}