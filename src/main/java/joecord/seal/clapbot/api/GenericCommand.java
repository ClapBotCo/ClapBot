package joecord.seal.clapbot.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import joecord.seal.clapbot.CommandHandler;
import net.dv8tion.jda.api.events.GenericEvent;

/**
 * Used to create a command
 */
public abstract class GenericCommand<E extends GenericEvent> {

    /* Fields --------------------------------------------------------------- */

    protected String displayName = null;
    protected String description = null;

    /**
     * The {@code Class<T extends GenericEvent>} that this command concerns.
     * Must be set by the constructor.
     */
    protected Class<E> eventClass;

    /**
     * A set of the {@link joecord.seal.clapbot.api.CommandProperty 
     * CommandProperty}'s that this command has registered.
     */
    private EnumSet<CommandProperty> properties =
        EnumSet.noneOf(CommandProperty.class);

    /**
     * For commands with the CommandProperty {@link 
     * joecord.seal.clapbot.api.CommandProperty#INVOKED INVOKED}}. Contains
     * all Strings that can be used to invoke the command.
     */
    private HashSet<String> aliases = null;
    /**
     * For commands with the CommandProperty {@link 
     * joecord.seal.clapbot.api.CommandProperty#CONDITIONAL CONDITIONAL}}.
     * Is a predicate that takes in the generic {@code <T extends Event>} that
     * the command uses that returns true iff the condition passes, i.e., the
     * command should be triggered.
     */
    private Predicate<E> condition = null;
    private String conditionDesc = null;
    /**
     * For commands with the CommandProperty {@link 
     * joecord.seal.clapbot.api.CommandProperty#PRIVELAGED PRIVELAGED}}.
     * Is a predicate that takes in the generic {@code <T extends Event>} that
     * the command uses that returns true iff the users are privelaged, i.e.,
     * the command should be triggered.
     */
    private Predicate<E> privelage = null;
    private String privelageDesc = null;
    /**
     * For commands with the CommandProperty {@link 
     * joecord.seal.clapbot.api.CommandProperty#META META}. Is the {@link 
     * joecord.seal.clapbot.CommandHandler Command Handler} that this instance
     * uses.
     */
    private CommandHandler handler = null;
    /**
     * For commands with the CommandProperty {@link 
     * joecord.seal.clapbot.api.CommandProperty#USES_ARGUMENTS USES_ARGUMENTS}}.
     * Contains the words given after the command alias in the invoking
     * message, split by space.
     */
    private String[] arguments = null;
    private String argumentsDesc = null;

    /* Constructors --------------------------------------------------------- */

    /**
     * Construct a new GenericCommand with the given event class and registered
     * command properties.
     * @param eventClass The subclass of GenericEvent that this command
     * responds to
     * @param properties Zero or more CommandPropertys to register
     * @throws IllegalArgumentException If the registered command properties
     * are illegal
     */
    public GenericCommand(Class<E> eventClass, CommandProperty... properties) {
        this.eventClass = eventClass;
        this.properties.addAll(Arrays.asList(properties));

        CommandProperty.assertLegality(properties);
    }

    /**
     * Construct a new GenericCommand with the given event class and registered
     * command properties.
     * @param eventClass The subclass of GenericEvent that this command
     * responds to
     * @param properties A collection of CommandPropertys to register
     * @throws IllegalArgumentException If the registered command properties
     * are illegal
     */
    public GenericCommand(Class<E> eventClass,
        Collection<CommandProperty> properties) {

        this.eventClass = eventClass;
        this.properties.addAll(properties);

        CommandProperty.assertLegality(properties);
    }

    /* Abstract execute method ---------------------------------------------- */

    /**
     * Called when a command should be executed on an event.
     * @param event The JDA Event to work with
     */
    public abstract void execute(E event);

    /* Getters -------------------------------------------------------------- */

    /**
     * Get the display name of the command to be shown to the user. If not set,
     * this will return null.
     * @return Name string
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Gets the display name of the command to be shown to the user. Equivalent
     * to {@link joecord.seal.clapbot.api.GenericCommand#getDisplayName()
     * getDisplayName()}. If not set, this will return null.
     * @return Name string
     */
    @Override
    public String toString() {
        return getDisplayName();
    }

    /**
     * Get a string describing what the command does. If not set, this will
     * return null.
     * @return Description string
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the {@code Class<T extends GenericEvent>} that this command concerns.
     * @return Event class
     */
    public Class<E> getEventClass() {
        return this.eventClass;
    }

    /**
     * Returns an unmodifiable version of the properties set for this command.
     * @return (Unmodifiable) set of CommandProperties
     */
    public Set<CommandProperty> getProperties() {
        return Collections.unmodifiableSet(this.properties);
    }

    /**
     * Returns true iff all given properties are contained in this command's
     * properties set.
     * 
     * If no properties are specified then the check is vacuously true.
     * @param properties Zero or more properties to check
     * @return True iff the command has all given properties
     */
    public boolean hasProperties(CommandProperty... properties) {
        for(CommandProperty prop : properties) {
            if(!this.properties.contains(prop)) {
                return false;
            }
        }

        return true;
    }

    /* Handle CommandProperty.INVOKED --------------------------------------- */

    /**
     * Must be called by the command at construction time iff the command has
     * the property {@link joecord.seal.clapbot.api.CommandProperty#INVOKED
     * INVOKED}.
     * 
     * Sets the command's aliases, the strings that can be used in
     * conjunction with the command handler's prefix to invoke the command.
     * @param alias The first alias of the command
     * @param aliases Any additional aliases
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final protected void setAliases(String alias, String... aliases) {
        assertProperty(CommandProperty.INVOKED);

        this.aliases = new HashSet<>();
        this.aliases.add(alias);
        this.aliases.addAll(Arrays.asList(aliases));
    }

    /**
     * Must be called by the command at construction time iff the command has
     * the property {@link joecord.seal.clapbot.api.CommandProperty#INVOKED
     * INVOKED}.
     * 
     * Sets the command's aliases, the strings that can be used in
     * conjunction with the command handler's prefix to invoke the command.
     * @param aliases Collection of the aliases of the command with non-zero
     * size
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties OR if the given collection was of zero
     * size
     */
    final protected void setAliases(Collection<String> aliases) {
        assertProperty(CommandProperty.INVOKED);

        if(aliases.size() == 0) {
            throw new IllegalArgumentException(
                "Given alias collection must be non-zero size");
        }

        this.aliases = new HashSet<>(aliases);
    }

    /**
     * Gets the aliases of the command iff the command has the property {@link
     * joecord.seal.clapbot.api.CommandProperty#INVOKED INVOKED}.
     * @return Set of strings of the aliases
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     * @throws IllegalStateException If the command never set it's aliases
     */
    final public Set<String> getAliases() {
        assertProperty(CommandProperty.INVOKED);

        if(this.aliases == null) {
            throw new IllegalStateException(
                "Invoked command never set aliases");
        }
        else {
            return this.aliases;
        }
    }

    /* Handle CommandProprty.CONDITIONAL ------------------------------------ */

    /**
     * Must be called by the command at construction time iff the command has
     * the property {@link joecord.seal.clapbot.api.CommandProperty#CONDITIONAL
     * CONDITIONAL}.
     * 
     * Sets the predicate condition of the command, the predicate takes in the
     * event that the command concerns. Before executing, the command handler
     * checks the condition and only executes the command if it returns true.
     * @param condition The condition predicate
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties 
     */
    final protected void setCondition(Predicate<E> condition) {
        assertProperty(CommandProperty.CONDITIONAL);

        this.condition = condition;
    }

    /**
     * Checks the command's condition predicate with the given event iff the 
     * command has the property {@link 
     * joecord.seal.clapbot.api.CommandProperty#CONDITIONAL CONDITIONAL}.
     * @param event The event to check
     * @return True iff the condition passes on this event
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final public boolean checkCondition(E event) {
        assertProperty(CommandProperty.CONDITIONAL);

        if(this.condition == null) {
            throw new IllegalStateException(
                "Conditional command never set condition");
        }
        else {
            return this.condition.test(event);
        }
    }
    
    /**
     * Optionally called by commands with the property {@link 
     * joecord.seal.clapbot.api.CommandProperty#CONDITIONAL
     * CONDITIONAL}.
     * 
     * Sets a string to describe the requirements that events must meet to
     * pass the condition.
     * @param conditionDesc Condition description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final protected void setConditionDesc(String conditionDesc) {
        assertProperty(CommandProperty.CONDITIONAL);

        this.conditionDesc = conditionDesc;
    }

    /**
     * Gets the description string of the command's condition iff the command
     * has the property {@link
     * joecord.seal.clapbot.api.CommandProperty#CONDITIONAL CONDITIONAL}.
     * If the condition description was never set, this will return null.
     * @return Description string of the condition
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final public String getConditionDesc() {
        assertProperty(CommandProperty.CONDITIONAL);

        return this.conditionDesc;
    }

    /* Handle CommandProprty.PRIVELAGED ------------------------------------- */

    /**
     * Must be called by the command at construction time iff the command has
     * the property {@link joecord.seal.clapbot.api.CommandProperty#PRIVELAGED
     * PRIVELAGED}.
     * 
     * Sets the privelage predicate of the command, the predicate takes in the
     * event that the command concerns. Before executing, the command handler
     * checks the privelage and only executes the command if it returns true.
     * @param privelage The privelage predicate
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties 
     */
    final protected void setPrivelage(Predicate<E> privelage) {
        assertProperty(CommandProperty.PRIVELAGED);

        this.privelage = privelage;
    }

    /**
     * Checks the command's privelage predicate with the given event iff the 
     * command has the property {@link 
     * joecord.seal.clapbot.api.CommandProperty#PRIVELAGED PRIVELAGED}.
     * @param event The event to check
     * @return True iff the privelage passes on this event
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     * @throws IllegalStateException If the command never set it's privelage
     */
    final public boolean checkPrivelage(E event) {
        assertProperty(CommandProperty.PRIVELAGED);

        if(this.privelage == null) {
            throw new IllegalStateException(
                "Privelaged command never set privelage");
        }
        else {
            return this.privelage.test(event);
        }
    }

    /**
     * Optionally called by commands with the property {@link 
     * joecord.seal.clapbot.api.CommandProperty#PRIVELAGED PRIVELAGED}.
     * 
     * Sets a string to describe the requirements that events must meet to
     * pass the privelage check.
     * @param privelageDesc Privelage description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final protected void setPrivelageDesc(String privelageDesc) {
        assertProperty(CommandProperty.PRIVELAGED);

        this.privelageDesc = privelageDesc;
    }

    /**
     * Gets the description string of the command's condition iff the command
     * has the property {@link
     * joecord.seal.clapbot.api.CommandProperty#PRIVELAGED PRIVELAGED}.
     * If the privelage description was never set, this will return null.
     * @return Description string of the privelage check
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final public String getPrivelageDesc() {
        assertProperty(CommandProperty.PRIVELAGED);

        return this.privelageDesc;
    }

    /* Handle CommandProprty.META ------------------------------------------- */

    /**
     * Gives the command access to the {@link 
     * joecord.seal.clapbot.api.CommandHandler CommandHandler} iff the command
     * has the property {@link joecord.seal.clapbot.api.CommandProperty#META 
     * META}.
     * @param handler The command handler
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final public void setCommandHandler(CommandHandler handler) {
        assertProperty(CommandProperty.META);

        this.handler = handler;
    }

    /**
     * Gets the previosuly set {@link 
     * joecord.seal.clapbot.api.CommandHandler CommandHandler} iff the command
     * has the property {@link joecord.seal.clapbot.api.CommandProperty#META 
     * META}.
     * @return The command handler
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final protected CommandHandler getCommandHandler() {
        assertProperty(CommandProperty.META);

        if(this.handler == null) {
            throw new IllegalStateException(
                "Command handler never set command handler on meta command");
        }
        else {
            return this.handler;
        }
    }

    /**
     * Clears the {@link joecord.seal.clapbot.api.CommandHandler
     * CommandHandler} for use after a command has been executed for commands
     * with {@link joecord.seal.clapbot.api.CommandProperty#META 
     * META}.
     * 
     * This command does NOT fail when called on a command without this
     * property.
     */
    final public void clearCommandHandler() {
        this.handler = null;
    }

    /* Handle CommandProperty.USES_ARGUMENTS -------------------------------- */

    /**
     * Sets the command's arguments iff the command has the property {@link
     * joecord.seal.clapbot.api.CommandProperty#USES_ARGUMENTS USES_ARGUMENTS}.
     * @param arguments The arguments to give
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final public void setArguments(String[] arguments) {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        this.arguments = arguments;
    }

    /**
     * Gets the previosuly set arguments iff the command has the property {@link
     * joecord.seal.clapbot.api.CommandProperty#USES_ARGUMENTS USES_ARGUMENTS}.
     * @return The arguments
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     * @throws IllegalStateException If the arguments were never set
     */
    final protected String[] getArguments() {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        if(this.arguments == null) {
            throw new IllegalStateException(
                "Command handler never set arguments on Uses-Arguments command"
            );
        }
        else {
            return this.arguments;
        }
    }

    /**
     * Clears the command's arguments for use after the command has been
     * executed for commands with the property {@link
     * joecord.seal.clapbot.api.CommandProperty#USES_ARGUMENTS USES_ARGUMENTS}.
     * 
     * This command does NOT fail on commands without this property.
     */
    final public void clearArguments() {
        this.arguments = null;
    }

    /**
     * Optionally called by commands with the property {@link
     * joecord.seal.clapbot.api.CommandProperty#USES_ARGUMENTS USES_ARGUMENTS}.
     * 
     * Sets a string to describe the usage syntax of the command to show what
     * arguments are required. For example {@code echo <message to send>}.
     * @param argumentsDesc Arguments usage syntax description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final protected void setArgumentsDesc(String argumentsDesc) {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        this.argumentsDesc = argumentsDesc;
    }

    /**
     * Gets the arguments description string iff the command has the property 
     * {@link joecord.seal.clapbot.api.CommandProperty#USES_ARGUMENTS
     * USES_ARGUMENTS}. If the arguments description was never set, this will
     * return null.
     * 
     * The string describes the usage syntax of the command to show what
     * arguments are required. For example {@code echo <message to send>}.
     * @return Arguments usage syntax description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    final public String getArgumentsDesc() {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        return this.argumentsDesc;
    }

    /* Private convenience method ------------------------------------------- */

    private void assertProperty(CommandProperty... assertProps) {
        for(CommandProperty prop : assertProps) {
            if(!hasProperties(prop)) {
                throw new IllegalArgumentException(
                "Cannot perform " + prop.toString() + 
                " operation without declaring command property for command " +
                this.displayName + ".");
            }
        }
    }
}
