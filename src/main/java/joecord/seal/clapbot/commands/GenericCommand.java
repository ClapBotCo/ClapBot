package joecord.seal.clapbot.commands;

/**
 * Used to create a command
 */
public interface GenericCommand {

    /**
     * Called when a command should be executed
     * @param arguments The arguments sent in the command
     */
    void execute(String[] arguments);

    /**
     * Called when a command should be executed with no arguments
     */
    void execute();

    /**
     * Get the name of the command.
     * @return The name of the command
     */
    String getName();

    /**
     * Get the description for the command
     * For example {@code Repeats the arguments send to the command}
     * @return The description string for the command
     */
    String getDescription();
}
