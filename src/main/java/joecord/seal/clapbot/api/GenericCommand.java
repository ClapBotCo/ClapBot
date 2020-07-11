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
public abstract class GenericCommand<T extends GenericEvent> {

    protected String displayName = null;
    protected String description = null;

    /**
     * The {@code Class<T extends GenericEvent>} that this command concerns.
     * Must be set by the constructor.
     */
    protected Class<T> eventClass;

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
    private Predicate<T> condition = null;
    private String conditionDesc = null;
    /**
     * For commands with the CommandProperty {@link 
     * joecord.seal.clapbot.api.CommandProperty#PRIVELAGED PRIVELAGED}}.
     * Is a predicate that takes in the generic {@code <T extends Event>} that
     * the command uses that returns true iff the users are privelaged, i.e.,
     * the command should be triggered.
     */
    private Predicate<T> privelage = null;
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

    /**
     * Construct a new GenericCommand with the given event class and registered
     * command properties.
     * @param eventClass The subclass of GenericEvent that this command
     * responds to
     * @param properties Zero or more CommandPropertys to register
     */
    public GenericCommand(Class<T> eventClass, CommandProperty... properties) {
        this.eventClass = eventClass;
        this.properties.addAll(Arrays.asList(properties));
    }

    /**
     * Construct a new GenericCommand with the given event class and registered
     * command properties.
     * @param eventClass The subclass of GenericEvent that this command
     * responds to
     * @param properties A collection of CommandPropertys to register
     */
    public GenericCommand(Class<T> eventClass,
        Collection<CommandProperty> properties) {

        this.eventClass = eventClass;
        this.properties.addAll(properties);
    }

    /**
     * Called when a command should be executed on an event.
     * @param event The JDA Event to work with
     */
    public abstract void execute(T event);

    /**
     * Get the display name of the command to be shown to the user.
     * @return Name string
     * @throws IllegalStateException If the name was never set
     */
    public String getDisplayName() {
        if(this.displayName == null) {
            throw new IllegalStateException("Display name never set");
        }
        return this.displayName;
    }

    /**
     * Gets the display name of the command to be shown to the user. Equivalent
     * to {@link joecord.seal.clapbot.api.GenericCommand#getDisplayName()
     * getDisplayName()}.
     * @return Name string
     * @throws IllegalStateException If the name was never set
     */
    @Override
    public String toString() {
        return getDisplayName();
    }

    /**
     * Get a string describing what the command does.
     * @return Description string
     * @throws IllegalStateException If the description was never set
     */
    public String getDescription() {
        if(this.description == null) {
            throw new IllegalStateException("Description never set");
        }
        return this.description;
    }

    /**
     * Get the {@code Class<T extends GenericEvent>} that this command concerns.
     * @return Event class
     */
    public Class<T> getEventClass() {
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

    // Handle CommandProperty.INVOKED

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

    // Handle CommandProprty.CONDITIONAL

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
    final protected void setCondition(Predicate<T> condition) {
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
    final public boolean checkCondition(T event) {
        assertProperty(CommandProperty.CONDITIONAL);

        if(this.condition == null) {
            throw new IllegalStateException(
                "Conditional command never set condition");
        }
        else {
            return this.condition.test(event);
        }
    }
    
    // TODO document from here
    final protected void setConditionDesc(String conditionDesc) {
        assertProperty(CommandProperty.CONDITIONAL);

        this.conditionDesc = conditionDesc;
    }

    final public String getConditionDesc() {
        assertProperty(CommandProperty.CONDITIONAL);

        if(this.conditionDesc == null) {
            throw new IllegalStateException(
                "Conditional command never set condition description");
        }
        else {
            return this.conditionDesc;
        }
    }

    // Handle CommandProprty.PRIVELAGED

    final protected void setPrivelage(Predicate<T> privelage) {
        assertProperty(CommandProperty.PRIVELAGED);

        this.privelage = privelage;
    }

    final public boolean checkPrivelage(T event) {
        assertProperty(CommandProperty.PRIVELAGED);

        if(this.privelage == null) {
            throw new IllegalStateException(
                "Privelaged command never set privelage");
        }
        else {
            return this.privelage.test(event);
        }
    }

    final protected void setPrivelageDesc(String privelageDesc) {
        assertProperty(CommandProperty.PRIVELAGED);

        this.privelageDesc = privelageDesc;
    }

    final public String getPrivelageDesc() {
        assertProperty(CommandProperty.PRIVELAGED);

        if(this.privelageDesc == null) {
            throw new IllegalStateException(
                "Privelaged command never set privelage description");
        }
        else {
            return this.privelageDesc;
        }
    }

    // Handle CommandProprty.META

    final public void setCommandHandler(CommandHandler handler) {
        assertProperty(CommandProperty.META);

        this.handler = handler;
    }

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

    // Handle CommandProperty.USES_ARGUMENTS

    final public void setArguments(String[] arguments) {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        this.arguments = arguments;
    }

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

    final protected void setArgumentsDesc(String argumentsDesc) {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        this.argumentsDesc = argumentsDesc;
    }

    final public String getArgumentsDesc() {
        assertProperty(CommandProperty.USES_ARGUMENTS);

        if(this.argumentsDesc == null) {
            throw new IllegalStateException(
                "Uses-Arguments command never set arguments description");
        }
        else {
            return this.argumentsDesc;
        }
    }

    // Private convenience method

    private void assertProperty(CommandProperty property) {
        if(!this.properties.contains(property)) {
            throw new IllegalArgumentException(
                "Cannot perform " + property.toString() + 
                " operation without declaring command property for command " +
                this.displayName + ".");
        }
    }
}
