package joecord.seal.clapbot.api.legacy

import joecord.seal.clapbot.CommandHandler
import net.dv8tion.jda.api.events.GenericEvent
import java.util.*
import java.util.function.Predicate

/**
 * Used to create a command
 */
abstract class LegacyGenericCommand<E : GenericEvent> {
    /* Fields --------------------------------------------------------------- */
    @JvmField
    protected var displayName: String? = null

    @JvmField
    protected var description: String? = null

    /**
     * The `Class<T extends GenericEvent>` that this command concerns. Must be set by the
     * constructor.
     */
    var eventClass: Class<E>
        protected set

    /**
     * A set of the [CommandProperty][joecord.seal.clapbot.api.LegacyCommandProperty]'s that this command
     * has registered.
     */
    private val properties = EnumSet.noneOf(LegacyCommandProperty::class.java)

    /**
     * For commands with the CommandProperty
     * [INVOKED][joecord.seal.clapbot.api.LegacyCommandProperty.INVOKED]. Contains all Strings that can
     * be used to invoke the command.
     */
    private var aliases: HashSet<String>? = null

    /**
     * For commands with the CommandProperty
     * [CONDITIONAL][joecord.seal.clapbot.api.LegacyCommandProperty.CONDITIONAL].
     * Is a predicate that takes in the generic `<T extends Event>` that the command uses that
     * returns true iff the condition passes, i.e., the command should be triggered.
     */
    private var condition: Predicate<E>? = null
    private var conditionDesc: String? = null

    /**
     * For commands with the CommandProperty
     * [PRIVILEGED][joecord.seal.clapbot.api.LegacyCommandProperty.PRIVILEGED]}.
     * Is a predicate that takes in the generic `<T extends Event>` that the command uses that
     * returns true iff the users are privelaged, i.e., the command should be triggered.
     */
    private var privelage: Predicate<E>? = null
    private var privilegeDesc: String? = null

    /**
     * For commands with the CommandProperty [META][joecord.seal.clapbot.api.LegacyCommandProperty.META].
     * Is the [CommandHandler][joecord.seal.clapbot.CommandHandler] that this instance uses.
     */
    private var handler: CommandHandler? = null

    /**
     * For commands with the CommandProperty [USES_ARGUMENTS]
     * [joecord.seal.clapbot.api.LegacyCommandProperty.USES_ARGUMENTS].
     * Contains the words given after the command alias in the invoking
     * message, split by space.
     */
    private var arguments: Array<String>? = null
    private var argumentsDesc: String? = null
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
    constructor(eventClass: Class<E>, vararg properties: LegacyCommandProperty) {
        this.eventClass = eventClass
        this.properties.addAll(properties)
        LegacyCommandProperty.assertLegality(*properties)
    }

    /**
     * Construct a new GenericCommand with the given event class and registered
     * command properties.
     * @param eventClass The subclass of GenericEvent that this command
     * responds to
     * @param properties A collection of CommandProperties to register
     * @throws IllegalArgumentException If the registered command properties
     * are illegal
     */
    constructor(eventClass: Class<E>, properties: Collection<LegacyCommandProperty>) {
        this.eventClass = eventClass
        this.properties.addAll(properties)
        LegacyCommandProperty.assertLegality(properties)
    }

    /* Abstract execute method ---------------------------------------------- */
    /**
     * Called when a command should be executed on an event.
     * @param event The JDA Event to work with
     */
    abstract fun execute(event: E)

    /* Getters -------------------------------------------------------------- */

    /**
     * Gets the display name of the command to be shown to the user. Equivalent
     * to [ getDisplayName()][joecord.seal.clapbot.api.LegacyGenericCommand.getDisplayName]. If not set, this will return null.
     * @return Name string
     */
    override fun toString(): String {
        return this.displayName!!
    }

    /**
     * Returns an unmodifiable version of the properties set for this command.
     * @return (Unmodifiable) set of CommandProperties
     */
    fun getProperties(): Set<LegacyCommandProperty> {
        return Collections.unmodifiableSet(properties)
    }

    /**
     * Returns true iff all given properties are contained in this command's
     * properties set.
     *
     * If no properties are specified then the check is vacuously true.
     * @param properties Zero or more properties to check
     * @return True iff the command has all given properties
     */
    fun hasProperties(vararg properties: LegacyCommandProperty): Boolean {
        for (prop in properties) {
            if (prop !in this.properties) {
                return false
            }
        }
        return true
    }
    /* Handle CommandProperty.INVOKED --------------------------------------- */
    /**
     * Must be called by the command at construction time iff the command has
     * the property [ INVOKED][joecord.seal.clapbot.api.LegacyCommandProperty.INVOKED].
     *
     * Sets the command's aliases, the strings that can be used in
     * conjunction with the command handler's prefix to invoke the command.
     * @param alias The first alias of the command
     * @param aliases Any additional aliases
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setAliases(alias: String, vararg aliases: String) {
        assertProperty(LegacyCommandProperty.INVOKED)
        this.aliases = HashSet()
        this.aliases!!.add(alias)
        this.aliases!!.addAll(aliases.toList())
    }

    /**
     * Must be called by the command at construction time iff the command has
     * the property [ INVOKED][joecord.seal.clapbot.api.LegacyCommandProperty.INVOKED].
     *
     * Sets the command's aliases, the strings that can be used in
     * conjunction with the command handler's prefix to invoke the command.
     * @param aliases Collection of the aliases of the command with non-zero
     * size
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties OR if the given collection was of zero
     * size
     */
    fun setAliases(aliases: Collection<String>) {
        assertProperty(LegacyCommandProperty.INVOKED)
        require(aliases.isNotEmpty()) { "Given alias collection must be non-zero size" }
        this.aliases = HashSet(aliases)
    }

    /**
     * Gets the aliases of the command iff the command has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.INVOKED].
     * @return Set of strings of the aliases
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     * @throws IllegalStateException If the command never set it's aliases
     */
    fun getAliases(): Set<String>? {
        assertProperty(LegacyCommandProperty.INVOKED)
        return if (aliases == null) {
            throw IllegalStateException(
                    "Invoked command never set aliases")
        } else {
            aliases
        }
    }
    /* Handle CommandProprty.CONDITIONAL ------------------------------------ */
    /**
     * Must be called by the command at construction time iff the command has
     * the property [ CONDITIONAL][joecord.seal.clapbot.api.LegacyCommandProperty.CONDITIONAL].
     *
     * Sets the predicate condition of the command, the predicate takes in the
     * event that the command concerns. Before executing, the command handler
     * checks the condition and only executes the command if it returns true.
     * @param condition The condition predicate
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setCondition(condition: Predicate<E>?) {
        assertProperty(LegacyCommandProperty.CONDITIONAL)
        this.condition = condition
    }

    /**
     * Checks the command's condition predicate with the given event iff the
     * command has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.CONDITIONAL].
     * @param event The event to check
     * @return True iff the condition passes on this event
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun checkCondition(event: E): Boolean {
        assertProperty(LegacyCommandProperty.CONDITIONAL)
        return condition?.test(event) ?: error("Conditional command never set condition")
    }

    /**
     * Optionally called by commands with the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.CONDITIONAL].
     *
     * Sets a string to describe the requirements that events must meet to
     * pass the condition.
     * @param conditionDesc Condition description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setConditionDesc(conditionDesc: String?) {
        assertProperty(LegacyCommandProperty.CONDITIONAL)
        this.conditionDesc = conditionDesc
    }

    /**
     * Gets the description string of the command's condition iff the command
     * has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.CONDITIONAL].
     * If the condition description was never set, this will return null.
     * @return Description string of the condition
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun getConditionDesc(): String? {
        assertProperty(LegacyCommandProperty.CONDITIONAL)
        return conditionDesc
    }
    /* Handle CommandProprty.PRIVILEGED ------------------------------------- */
    /**
     * Must be called by the command at construction time iff the command has
     * the property [ PRIVILEGED][joecord.seal.clapbot.api.LegacyCommandProperty.PRIVILEGED].
     *
     * Sets the privilege predicate of the command, the predicate takes in the
     * event that the command concerns. Before executing, the command handler
     * checks the privilege and only executes the command if it returns true.
     * @param privilege The privilege predicate
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setPrivilege(privilege: Predicate<E>?) {
        assertProperty(LegacyCommandProperty.PRIVILEGED)
        this.privelage = privilege
    }

    /**
     * Checks the command's privilege predicate with the given event iff the
     * command has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.PRIVILEGED].
     * @param event The event to check
     * @return True iff the privilege passes on this event
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     * @throws IllegalStateException If the command never set it's privelage
     */
    fun checkPrivilege(event: E): Boolean {
        assertProperty(LegacyCommandProperty.PRIVILEGED)
        return privelage?.test(event) ?: error("Privileged command never set privilege")
    }

    /**
     * Optionally called by commands with the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.PRIVILEGED].
     *
     * Sets a string to describe the requirements that events must meet to
     * pass the privelage check.
     * @param privilegeDesc Privelage description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setPrivilegeDesc(privilegeDesc: String?) {
        assertProperty(LegacyCommandProperty.PRIVILEGED)
        this.privilegeDesc = privilegeDesc
    }

    /**
     * Gets the description string of the command's condition iff the command
     * has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.PRIVILEGED].
     * If the privelage description was never set, this will return null.
     * @return Description string of the privelage check
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun getPrivelageDesc(): String? {
        assertProperty(LegacyCommandProperty.PRIVILEGED)
        return privilegeDesc
    }
    /* Handle CommandProprty.META ------------------------------------------- */
    /**
     * Gives the command access to the [ ] iff the command
     * has the property [ META][joecord.seal.clapbot.api.LegacyCommandProperty.META].
     * @param handler The command handler
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setCommandHandler(handler: CommandHandler?) {
        assertProperty(LegacyCommandProperty.META)
        this.handler = handler
    }

    /**
     * Gets the previosuly set [ ] iff the command
     * has the property [ META][joecord.seal.clapbot.api.LegacyCommandProperty.META].
     * @return The command handler
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun getCommandHandler(): CommandHandler? {
        assertProperty(LegacyCommandProperty.META)
        return if (handler == null) {
            throw IllegalStateException(
                    "Command handler never set command handler on meta command")
        } else {
            handler
        }
    }

    /**
     * Clears the [ CommandHandler][joecord.seal.clapbot.api.CommandHandler] for use after a command has been executed for commands
     * with [ META][joecord.seal.clapbot.api.LegacyCommandProperty.META].
     *
     * This command does NOT fail when called on a command without this
     * property.
     */
    fun clearCommandHandler() {
        handler = null
    }
    /* Handle CommandProperty.USES_ARGUMENTS -------------------------------- */
    /**
     * Sets the command's arguments iff the command has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.USES_ARGUMENTS].
     * @param arguments The arguments to give
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setArguments(arguments: Array<String>?) {
        assertProperty(LegacyCommandProperty.USES_ARGUMENTS)
        this.arguments = arguments
    }

    /**
     * Gets the previosuly set arguments iff the command has the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.USES_ARGUMENTS].
     * @return The arguments
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     * @throws IllegalStateException If the arguments were never set
     */
    fun getArguments(): Array<String>? {
        assertProperty(LegacyCommandProperty.USES_ARGUMENTS)
        return if (arguments == null) {
            throw IllegalStateException(
                    "Command handler never set arguments on Uses-Arguments command"
            )
        } else {
            arguments
        }
    }

    /**
     * Clears the command's arguments for use after the command has been
     * executed for commands with the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.USES_ARGUMENTS].
     *
     * This command does NOT fail on commands without this property.
     */
    fun clearArguments() {
        arguments = null
    }

    /**
     * Optionally called by commands with the property [ ][joecord.seal.clapbot.api.LegacyCommandProperty.USES_ARGUMENTS].
     *
     * Sets a string to describe the usage syntax of the command to show what
     * arguments are required. For example `echo <message to send>`.
     * @param argumentsDesc Arguments usage syntax description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun setArgumentsDesc(argumentsDesc: String?) {
        assertProperty(LegacyCommandProperty.USES_ARGUMENTS)
        this.argumentsDesc = argumentsDesc
    }

    /**
     * Gets the arguments description string iff the command has the property
     * [ USES_ARGUMENTS][joecord.seal.clapbot.api.LegacyCommandProperty.USES_ARGUMENTS]. If the arguments description was never set, this will
     * return null.
     *
     * The string describes the usage syntax of the command to show what
     * arguments are required. For example `echo <message to send>`.
     * @return Arguments usage syntax description string
     * @throws IllegalArgumentException If this method was called on a command
     * without the required properties
     */
    fun getArgumentsDesc(): String? {
        assertProperty(LegacyCommandProperty.USES_ARGUMENTS)
        return argumentsDesc
    }

    /* Private convenience method ------------------------------------------- */
    private fun assertProperty(vararg assertProps: LegacyCommandProperty) {
        for (prop in assertProps) {
            require(hasProperties(prop)) {
                "Cannot perform " + prop.toString() +
                        " operation without declaring command property for command " +
                        displayName + "."
            }
        }
    }
}