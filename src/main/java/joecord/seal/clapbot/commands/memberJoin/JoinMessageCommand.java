package joecord.seal.clapbot.commands.memberJoin;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

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
        MessageBuilder msg = new MessageBuilder();
        msg.append(event.getUser());
        msg.append(" has joined us! CLAP CLAP CLAP");

        event.getGuild()
            .getTextChannelById(this.channelId)
            .sendMessage(msg.build()).queue();
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