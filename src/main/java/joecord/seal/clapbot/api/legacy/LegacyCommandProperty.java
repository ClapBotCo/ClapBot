package joecord.seal.clapbot.api.legacy;

import java.util.Arrays;
import java.util.Collection;

public enum LegacyCommandProperty {

    /**
     * Command is called by sending a message starting with the prefix and
     * then one of the command's aliases.
     * 
     * Invoked commands must call {@link 
     * LegacyGenericCommand#setAliases
     * setAliases(HashSet<String>)} with their aliases at construction time.
     */
    INVOKED,

    /**
     * Command has a condition that is checked before being executed.
     * 
     * Conditional commands must call {@link
     * LegacyGenericCommand#setCondition
     * setCondition(Predicate)} with their condition predicate and {@link
     * LegacyGenericCommand#setConditionDesc
     * setConditionDesc(String)} with a description of the condition at 
     * construction time.
     */
    CONDITIONAL,

    /**
     * Command can only be invoked by certain users.
     * 
     * Privileged commands must call {@link
     * LegacyGenericCommand#setPrivilege
     * setPrivelage(Predicate)} with their privelage check predicate and {@link
     * LegacyGenericCommand#setPrivilegeDesc
     * setPrivelageDesc(String)} with a description if the check at
     * construction time.
     */
    PRIVILEGED,

    /**
     * Command needs access to the {@link joecord.seal.clapbot.CommandHandler
     * Command Handler}.
     * 
     * Meta commands can call {@link 
     * LegacyGenericCommand#getCommandHandler
     * getCommandHandler()} to get the Command Handler at any time.
     */
    META,

    /**
     * Command needs access to arguments given in the invoking message.
     * 
     * Commands that use arguments must also be {@link 
     * LegacyCommandProperty#INVOKED
     * INVOKED} commands.
     * 
     * Uses-Arguments commands can call {@link 
     * LegacyGenericCommand#getArguments
     * getArguments()} to get their arguments at any time.
     * 
     * Uses-Arguments
     * commands must call {@link 
     * LegacyGenericCommand#setArgumentsDesc
     * setArgumentsDesc(String)} with a description of the command's usage
     * syntax at construction time.
     */
    USES_ARGUMENTS,
    
    /**
     * Command will be triggered even if the user that the event concerns is
     * a bot.
     */
    RESPECT_BOTS;

    @Override
    public String toString() {
        String[] display = this.name().split("_");

        // Convert to a nicer-to-read format
        for(int i = 0; i < display.length; i++) {
            display[i] = display[i].toLowerCase();
            display[i] =
                Character.toUpperCase(display[i].charAt(0)) +
                display[i].substring(1);
        }

        return String.join("-", display);
    }

    public static void assertLegality(LegacyCommandProperty... properties) {
        assertLegality(Arrays.asList(properties));
    }

    public static void assertLegality(Collection<LegacyCommandProperty> properties) {
        for(LegacyCommandProperty prop : properties) {
            switch(prop) {
                case USES_ARGUMENTS: {
                    // Requires INVOKED
                    if(!properties.contains(LegacyCommandProperty.INVOKED)) {
                        throw new IllegalArgumentException(
                            "Illegal CommandPropertys, cannot have " + 
                            "Uses-Arguments without Invoked");
                    }
                    break;
                }
                case CONDITIONAL:
                    break;
                case INVOKED:
                    break;
                case META:
                    break;
                case PRIVILEGED:
                    break;
                case RESPECT_BOTS:
                    break;
                default:
                    break;
            }
        }
    }
}