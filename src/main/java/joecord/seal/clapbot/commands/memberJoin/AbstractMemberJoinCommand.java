package joecord.seal.clapbot.commands.memberJoin;

import joecord.seal.clapbot.commands.GenericCommand;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public abstract class AbstractMemberJoinCommand implements 
    GenericCommand<GuildMemberJoinEvent> {

    protected String name;
    protected String description;

    /**
     * Construct a new message command.
     * @param name The name of the command
     * @param description The description string of the command
     */
    public AbstractMemberJoinCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

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