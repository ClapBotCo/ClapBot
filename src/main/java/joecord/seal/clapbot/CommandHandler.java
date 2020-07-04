package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.LinkedList;
import java.util.List;

public class CommandHandler implements EventListener {

    private String prefix;
    // TODO Data structure(s) to map event types to multiple stored commands

    /**
     * Construct a new command handler.
     * @param prefix The prefix used for message commands, for example with
     * prefix {@code !}, an invokable command called {@code command} would be
     * called by sending {@code !command} 
     */
    public CommandHandler(String prefix) {
        this.prefix = prefix;
        // TODO Initialise Data structure(s)
    }

    public void register(/* A command */) {
        // TODO Add the command to the data structure(s)
    }

    @Override
    public void onEvent(GenericEvent event) {
        /* TODO Iterate through the data structures for this event type, do
         * specific actions based on their properties and call execute() on
         * them */ 
    }
    
    public String getPrefix() {
        return this.prefix;
    }

    public List<GatewayIntent> getIntents() {
        LinkedList<GatewayIntent> intents = new LinkedList<>();

        /* TODO Iterate throgh the registered commands and resolve a list
         * of gateway intents that the JDA needs */

        return intents;
    }

    /**
     * Register a message command
     * @param command The command to register
     */
    /* Old code
    public void register(AbstractMessageCommand command) {
        if(!messageCommands.containsKey(command.getDisplayName())) {
            messageCommands.put(command.getDisplayName(), command);

            String[] aliases = command.getAliases();

            if(aliases.length != 0) {
                for(String alias : aliases) {
                    if(!messageCommands.containsKey(alias)) {
                        messageCommands.put(alias, command);
                    }
                }
            }
        }
    } */

    /* Old Code
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw().trim();

        // Logging
        System.out.print(event.getChannel().getName() + " - "
            + event.getAuthor().getName() + ": "
            + message + "\n");

        // Ignore bots
        if(event.getAuthor().isBot()) return;

        if(message.startsWith(prefix)) { // Handle message commands

            String[] parts = message.substring(prefix.length()).split(" ");
                // Parts of the message with the prefix removed
            String commandName = parts[0];
            String[] args = new String[] {};

            if(messageCommands.containsKey(commandName)) {

                // Get arguments if there is any
                if(parts.length > 1) {
                    args = Arrays.copyOfRange(parts, 1, parts.length);
                }

                // Get the command and execute it
                messageCommands.get(commandName).execute(event, args);
            }
        }
        else { // Handle conditional commands
            for(AbstractConditionalCommand command : conditionalCommands) {
                if(command.check(event)) {
                    command.execute(event);
                }
            }
        }
    } */

    /* Old code
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        // Logging
        System.out.println(event.getUser().getName() + " just joined");

        for(AbstractMemberJoinCommand command : memberJoinCommands) {
            command.execute(event);
        }
    } */

    /* Old code
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        // Logging
        System.out.println(event.getUser().getName() + " just reacted " +
            event.getReactionEmote().getName() + " to message id " + 
            event.getMessageId());

            for(AbstractReactionAddCommand command : reactionAddCommands) {
                command.execute(event);
            }
    } */
}
