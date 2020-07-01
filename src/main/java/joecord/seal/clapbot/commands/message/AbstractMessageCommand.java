package joecord.seal.clapbot.commands.message;

import joecord.seal.clapbot.commands.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractMessageCommand implements 
    GenericCommand<MessageReceivedEvent> {

    protected String name;
    protected String description;
    protected String usage;
    protected String[] aliases;

    /**
     * Construct a new message command.
     * @param name The name of the command
     * @param description The description string of the command
     * @param usage The command's usage syntax
     * @param aliases All of the command's aliases
     */
    public AbstractMessageCommand(String name, String description, String usage,
        String[] aliases) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        execute(event, new String[] {});
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Get the syntax of the command, including any arguments that need to be
     * given to the command. For example {@code echo <message to echo>} for the
     * echo command.
     * @return Usage string
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Get all aliases of the command. An alias is an alternative name that
     * can be used to call the command rather than using it's standard name.
     * @return Array of alias strings
     */
    public String[] getAliases() {
        return aliases.clone();
    }
}