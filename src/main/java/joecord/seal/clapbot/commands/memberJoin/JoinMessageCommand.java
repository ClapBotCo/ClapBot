package joecord.seal.clapbot.commands.memberJoin;

import joecord.seal.clapbot.commands.message.Embed;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import static joecord.seal.clapbot.commands.message.Embed.eb;

public class JoinMessageCommand extends AbstractMemberJoinCommand {

    /** The channel ID to send join messages to */
    private String channelId;
    
    /**
     * Construct a new join message command.
     * @param channelId The channel ID for join messages to be sent to
     */
    public JoinMessageCommand(String channelId) {
        this.name = "Join message";
        this.description = "Sends a join message whenever a new user joins";
        this.channelId = channelId;
    }

    @Override
    public void execute(GuildMemberJoinEvent event) {
        Embed embed = new Embed("New member!", "Test", "Hey!");

        event.getGuild()
            .getTextChannelById(this.channelId)
            .sendMessage(eb.build()).queue();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        JoinMessageCommand other;

        if(obj instanceof JoinMessageCommand) {
            other = (JoinMessageCommand)obj;

            if(super.equals(other) &&
                other.channelId.equals(this.channelId)) {
                    
                equal = true;
            }
        }

        return equal;
    }
}