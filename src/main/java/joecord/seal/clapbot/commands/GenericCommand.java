package joecord.seal.clapbot.commands;

import net.dv8tion.jda.api.events.Event;

/**
 * Used to create a command
 */
public interface GenericCommand<T extends Event> {

    /**
     * Called when a command should be executed
     * @param arguments The arguments sent in the command
     */
    void execute(T event, String[] arguments);

    /**
     * Called when a command should be executed with no arguments
     */
    void execute(T event);

    /**
     * Get the name of the command.
     * @return Name string
     */
    String getName();

    /**
     * Get a string describing the command.
     * @return Description string
     */
    String getDescription();
}
