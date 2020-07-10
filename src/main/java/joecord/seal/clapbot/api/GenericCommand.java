package joecord.seal.clapbot.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import joecord.seal.clapbot.CommandHandler;
import net.dv8tion.jda.api.events.Event;

/**
 * Used to create a command
 */
public abstract class GenericCommand<T extends Event> {

    protected String displayName = null;
    protected String description = null;

    protected Class<T> eventClass = null;

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
     * Construct a new GenericCommand with no registered command properties.
     */
    public GenericCommand() {}

    /**
     * Construct a new GenericCommand with the given registered command
     * properties.
     * @param properties A collection of CommandPropertys to register
     */
    public GenericCommand(Collection<CommandProperty> properties) {
        this.properties.addAll(properties);
    }

    /**
     * Construct a new GenericCommand with the given registered command
     * properties.
     * @param properties Varags or array of CommandPropertys to register
     */
    public GenericCommand(CommandProperty... properties) {
        this.properties.addAll(Arrays.asList(properties));
    }

    /**
     * Called when a command should be executed on an event.
     * @param event The JDA Event to work with
     */
    public abstract void execute(T event);

    /**
     * Get the display name of the command to be shown to the user.
     * @return Name string
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Get a string describing what the command does.
     * @return Description string
     */
    public String getDescription() {
        return this.description;
    }

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
     * @param property The first property to check
     * @param properties Any additional properties to check
     * @return True iff the command has all given properties
     */
    public boolean hasProperties(CommandProperty property,
        CommandProperty... properties) {
        for(CommandProperty prop : properties) {
            if(!this.properties.contains(prop)) {
                return false;
            }
        }

        return true;
    }

    // Handle CommandProperty.INVOKED

    final protected void setAliases(String... aliases) {
        assertProperty(CommandProperty.INVOKED);

        this.aliases = new HashSet<>(Arrays.asList(aliases));
    }

    final protected void setAliases(Collection<String> aliases) {
        assertProperty(CommandProperty.INVOKED);

        this.aliases = new HashSet<>(aliases);
    }

    final public HashSet<String> getAliases() {
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

    final protected void setCondition(Predicate<T> condition) {
        assertProperty(CommandProperty.CONDITIONAL);

        this.condition = condition;
    }

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
                " operation without declaring command property");
        }
    }
}
