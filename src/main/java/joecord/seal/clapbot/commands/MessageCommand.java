package joecord.seal.clapbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class MessageCommand extends MessageReceivedEvent implements
    GenericCommand {

    public MessageCommand(MessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());
    }

    @Override
    public void execute() {
        execute(new String[] {});
    }

    /**
     * Gets the syntax of the command, including any arguments that need to be
     * given to the command. For example {@code echo <message to echo>} for the
     * echo command.
     * @return Usage string
     */
    public abstract String getUsage();

    /**
     * Gets all aliases of the command. An alias is an alternative name that
     * can be used to call the command rather than using it's standard name.
     * @return Array of alias strings
     */
    public String[] getAliases() {
        return new String[] {};
    }
}