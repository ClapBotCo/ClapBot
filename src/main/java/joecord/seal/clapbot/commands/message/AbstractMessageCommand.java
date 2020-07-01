package joecord.seal.clapbot.commands.message;

import joecord.seal.clapbot.commands.GenericCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractMessageCommand implements 
    GenericCommand<MessageReceivedEvent> {

    /** The name of the command, used when the command is called */
    protected String name = "";
    /** The description string of the command, detailing what it does */
    protected String description = "";
    /** The usage syntax of the command, detailing how it is used */
    protected String usage = "";
    /** All of the command's aliases; alternate names that can be used to
     * call the command */
    protected String[] aliases = new String[0];

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