package joecord.seal.clapbot.commands.reactionAdd;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class AddRoleOnReactionAddCommand extends 
    GenericCommand<GuildMessageReactionAddEvent> {

    /** The ISnowflake ID of the message that this command watches reactions of
    */
    private String messageId;
    /** The reaction emote to look for for emojis, this is 
     * the codepoints of the emoji, for custom emotes it will be the name */
    private String emoteName;
    /** The ISnowflake ID of the role that this command will give */
    private String roleId;
    /** The JDA role resolved from the role ID */
    private Role role;
    /** True if the reaction emote is an emoji, otherwise false if it is a
     * custom emote */
    private boolean isEmoji;

    /**
     * Construct a new add role on reaction command
     * @param messageId The ISnowflake ID of the message to react to
     * @param emoteName The reaction emote to look for, for emojis, this is 
     * the codepoints of the emoji, eg. {@code U+1f973} for :partying_face:, 
     * for custom emotes it will be the name, eg. {@code joeclap} for :joeclap:
     * @param roleId The ISnowflake ID of the role that this command will give, 
     * must be lower in the role hierarchy than the highest role that the bot 
     * has
     */
    public AddRoleOnReactionAddCommand(String messageId, String emoteName,
        String roleId) {

        super(GuildMessageReactionAddEvent.class, CommandProperty.CONDITIONAL);
        
        this.displayName = "Add role on reaction add";
        this.description = "Gives a user that reacts to a specific message " +
            "with a specific emote a specific role";
        this.messageId = messageId;
        this.emoteName = emoteName;
        this.roleId = roleId;
        this.role = null;
        this.isEmoji = emoteName.startsWith("U+");

        super.setConditionDesc("Only activates on message ID " +
            this.messageId + " and with emote " + emoteName);
        super.setCondition(event -> checkMessageAndEmote(event));
    }


    @Override
    public void execute(GuildMessageReactionAddEvent event) {
        if(this.role == null) {
            this.role = event.getGuild().getRoleById(this.roleId);
        }

        event.getGuild().addRoleToMember(event.getUserId(), this.role)
            .queue();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        AddRoleOnReactionAddCommand other;

        if(obj instanceof AddRoleOnReactionAddCommand) {
            other = (AddRoleOnReactionAddCommand)obj;

            if(super.equals(other) &&
                other.messageId.equals(this.messageId) && 
                other.emoteName.equals(this.emoteName) &&
                other.roleId.equals(this.roleId)) {
                    
                equal = true;
            }
        }

        return equal;
    }

    /**
     * Check if the message ID matches this command's message ID and if the
     * emote matches this command's emote
     * @param event The GuildMessageReactionAddEvent to check
     * @return True iff the message and emote matches
     */
    private boolean checkMessageAndEmote(GuildMessageReactionAddEvent event) {
        ReactionEmote rm = event.getReactionEmote();

        if(
            // Check that it's reffering to the right message
            event.getMessageId().equals(this.messageId) && (
            (
                // If it's an emoji, check it's the right one
                (rm.isEmoji() && this.isEmoji) && 
                (rm.getAsCodepoints().equals(this.emoteName))
            )
            ||
            (
                // Otherwise, if it's an emote, check it's the right one
                (rm.isEmote() && !this.isEmoji) &&
                (rm.getName().equals(this.emoteName))
            )
        )) {
            return true;
        }
        
        return false;

    }
}