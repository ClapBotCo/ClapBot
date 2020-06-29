package joecord.seal.clapbot.commands;

import joecord.seal.clapbot.api.CommandExecutor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class CommandHandler {

    private String prefix;
    private HashMap<String, CommandExecutor> registeredCommands;

    public CommandHandler(String prefix){
        this.prefix = prefix;
        this.registeredCommands = new HashMap<>();
    }

    public boolean registerCommand(CommandExecutor commandExecutor){
        if(registeredCommands.containsKey(commandExecutor.getCommand())){
            return false;
        }
        registeredCommands.put(commandExecutor.getCommand(), commandExecutor);
        if(commandExecutor.getAliases() != null) {
            for (String s : commandExecutor.getAliases()) {
                if (!registeredCommands.containsKey(s)) {
                    registeredCommands.put(s, commandExecutor);
                }
            }
        }
        return true;
    }

    public void handleCommand(MessageChannel channel, User user, Member member,
        Message message) {

        String content = message.getContentRaw();
        String[] rawParts = content.split(" ");
            // Parts of the message including the prefix

        if(!content.startsWith(prefix)) { // Handle regex commands

            Optional<CommandExecutor> commandExecutor = 
                registeredCommands.values().stream()
                .filter(CommandExecutor::isRegexCommand)
                .filter((executor -> Pattern.matches(
                    executor.getCommand(), content.toLowerCase())))
                .findFirst();

            if(commandExecutor.isPresent()) System.out.println(
                "Recognised command " + commandExecutor.get().getCommand());

            commandExecutor.ifPresent(
                executor -> executor.execute(channel, user, member, rawParts));
        }
        else { // Handle non-regex commands

            String[] parts = content.substring(prefix.length()).split(" ");
                // Parts of the message with the prefix removed
            String commandName = parts[0];
            String[] args = new String[] {};

            if(registeredCommands.containsKey(commandName)) {

                System.out.println("Recognised command " + commandName);

                // Get arguments if there is any
                if(parts.length > 1) {
                    args = Arrays.copyOfRange(parts, 1, parts.length);
                }

                // Get the command executor
                CommandExecutor commandExecutor =
                    registeredCommands.get(commandName);
                
                // If it's a normal command, execute it
                if(!commandExecutor.isRegexCommand()) {
                    commandExecutor.execute(channel, user, member, args);
                }
            }
        }
    }
}
