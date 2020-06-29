package joecord.seal.clapbot.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Used to create a command
 */
public interface CommandExecutor {

    /**
     * Called when a command should be executed
     * @param channel       The channel where the command was executed
     * @param senderUser    The user that sent the command
     * @param senderMember  The member of the guild that sent the command
     * @param arguments     The arguments sent in the command
     */
    void execute(MessageChannel channel, User senderUser, Member senderMember, String[] arguments);

    /**
     * Get the main name of the command
     * @return  The name of the command
     */
    String getCommand();

    /**
     * A list of aliases for this command
     * @return  A list of aliases for this command
     */
    String[] getAliases();

    /**
     * Get the usage for the command
     * E.G {@code <argument1> <argument2>}
     * @return  The usage for the command
     */
    String getUsage();

    boolean isRegexCommand();
}
