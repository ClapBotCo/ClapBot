package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import joecord.seal.clapbot.commands.CommandDescriptor;
import joecord.seal.clapbot.commands.ConditionalCommand;
import joecord.seal.clapbot.commands.MessageCommand;

public class CommandHandler extends ListenerAdapter {

    private String prefix;
    private HashMap<String, CommandDescriptor<? extends MessageCommand>> messageCommands;
    private HashSet<CommandDescriptor<? extends ConditionalCommand>> conditionalCommands;

    public CommandHandler(String prefix){
        this.prefix = prefix;
        this.messageCommands = new HashMap<>();
        this.conditionalCommands = new HashSet<>();
    }

    public void registerMessageCommand(CommandDescriptor<? extends MessageCommand> command) {
        if(!messageCommands.containsKey(command.getName())) {
            messageCommands.put(command.getName(), command);

            if(command.getAliases().length != 0) {
                for(String alias : command.getAliases()) {
                    if(!messageCommands.containsKey(alias)) {
                        messageCommands.put(alias, command);
                    }
                }
            }
        }
    }

    public void registerConditonalCommand(CommandDescriptor<? extends ConditionalCommand> command) {
        if(!conditionalCommands.contains(command)) {
            conditionalCommands.add(command);
        }
    }

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

                // Get the command
                MessageCommand command =
                    messageCommands.get(commandName)
                    .getInstance(
                        new Class<?>[] {MessageReceivedEvent.class}, 
                        new Object[] {event});
                
                // Execute it
                command.execute(args);
            }
        }
        else { // Handle conditional commands
            for(CommandDescriptor<? extends ConditionalCommand> cd : conditionalCommands) {
                ConditionalCommand command = cd.getInstance(
                    new Class<?>[] {MessageReceivedEvent.class}, 
                    new Object[] {event});
                if(command.check()) {
                    command.execute();
                }
            }
        }
    }
}
