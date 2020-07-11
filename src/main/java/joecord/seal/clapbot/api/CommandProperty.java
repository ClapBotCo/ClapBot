package joecord.seal.clapbot.api;

public enum CommandProperty {

    /**
     * Command is called by sending a message starting with the prefix and
     * then one of the command's aliases.
     * 
     * Invoked commands must call {@link 
     * joecord.seal.clapbot.api.GenericCommand#setAliases 
     * setAliases(HashSet<String>)} with their aliases at construction time.
     */
    INVOKED,

    /**
     * Command has a condition that is checked before being executed.
     * 
     * Conditional commands must call {@link
     * joecord.seal.clapbot.api.GenericCommand#setCondition 
     * setCondition(Predicate)} with their condition predicate and {@link
     * joecord.seal.clapbot.api.GenericCommand#setConditionDesc 
     * setConditionDesc(String)} with a description of the condition at 
     * construction time.
     */
    CONDITIONAL,

    /**
     * Command can only be invoked by certain users.
     * 
     * Privelaged commands must call {@link
     * joecord.seal.clapbot.api.GenericCommand#setPrivelage 
     * setPrivelage(Predicate)} with their privelage check predicate and {@link
     * joecord.seal.clapbot.api.GenericCommand#setPrivelageDesc 
     * setPrivelageDesc(String)} with a description if the check at
     * construction time.
     */
    PRIVELAGED,

    /**
     * Command needs access to the {@link joecord.seal.clapbot.CommandHandler
     * Command Handler}.
     * 
     * Meta commands can call {@link 
     * joecord.seal.clapbot.api.GenericCommand#getCommandHandler
     * getCommandHandler()} to get the Command Handler at any time.
     */
    META,

    /**
     * Command needs access to arguments given in the invoking message.
     * 
     * Commands that use arguments must also be {@link 
     * joecord.seal.clapbot.api.CommandProperty#INVOKED 
     * INVOKED} commands.
     * 
     * Uses-Arguments commands can call {@link 
     * joecord.seal.clapbot.api.GenericCommand#getArguments
     * getArguments()} to get their arguments at any time.
     * 
     * Uses-Arguments
     * commands must call {@link 
     * joecord.seal.clapbot.api.GenericCommand#setArgumentsDesc
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
}