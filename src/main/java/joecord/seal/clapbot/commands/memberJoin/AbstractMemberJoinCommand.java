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
}