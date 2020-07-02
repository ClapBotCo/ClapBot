package joecord.seal.clapbot.commands.reactionAdd;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class AddRoleOnReactionAddCommand extends AbstractReactionAddCommand {

    private String messageId;
    private String reactionName;
    private String roleId;
    private Role role;

    /**
     * Construct a new add role on reaction command
     * @param messageId The ID of the message to react to
     * @param reactionName The name of the reaction to look for, for emojis,
     * this is the unicode of the emoji, for custom emotes it will be the name,
     * for example {@code joeclap} for {@code :joeclap:}
     * @param roleId The ID of the role that this command will give, must be
     * lower in the role hierarchy than the highest role that ClapBot has
     */
    public AddRoleOnReactionAddCommand(String messageId, String reactionName,
        String roleId) {
        
        this.name = "Add role on reaction add";
        this.description = "Gives a user that reacts to a specific message " +
            "with a specific emote a specific role";
        this.messageId = messageId;
        this.reactionName = reactionName;
        this.roleId = roleId;
        this.role = null;
    }


    @Override
    public void execute(GuildMessageReactionAddEvent event) {

        System.out.println(String.format("Testing message id %s vs %s",
            messageId, event.getMessageId()));
        System.out.println(String.format("Testing reaction %s vs %s",
            reactionName, event.getReactionEmote().getName()));

        if(event.getMessageId().equals(this.messageId) &&
            event.getReactionEmote().getName().equals(reactionName)) {

            System.out.println("Running the command");

            if(this.role == null) {
                this.role = event.getGuild().getRoleById(this.roleId);
            }

            event.getGuild().addRoleToMember(event.getUserId(), this.role);
        }
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        AddRoleOnReactionAddCommand other;

        if(obj instanceof AddRoleOnReactionAddCommand) {
            other = (AddRoleOnReactionAddCommand)obj;

            if(super.equals(other) &&
                other.messageId.equals(this.messageId) && 
                other.reactionName.equals(this.reactionName) &&
                other.roleId.equals(this.roleId)) {
                    
                equal = true;
            }
        }

        return equal;
    }
}