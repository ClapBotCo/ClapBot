package joecord.seal.clapbot.commands;

/**
 * Used to create a command
 */
public interface GenericCommand {

    /**
     * The name of the command
     */
    public static final String NAME = "";
    /**
     * A string describing the command
     */
    public static final String DESCRIPTION = "";
    /**
     * All aliases of the command. An alias is an alternative name that
     * can be used to call the command rather than using it's standard name.
     */
    public static final String[] ALIASES = new String[0];
    /**
     * The syntax of the command, including any arguments that need to be
     * given to the command. For example {@code echo <message to echo>} for the
     * echo command.
     */
    public static final String USAGE = "";

    /**
     * Called when a command should be executed
     * @param arguments The arguments sent in the command
     */
    void execute(String[] arguments);

    /**
     * Called when a command should be executed with no arguments
     */
    void execute();
}
