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
        String[] parts = content.split(" ");

        // Handle regex commands
        if(!content.startsWith(prefix)){
            Optional<CommandExecutor> commandExecutor = 
                registeredCommands.values().stream()
                .filter(CommandExecutor::isRegexCommand)
                .filter((executor -> Pattern.matches(
                    executor.getCommand(), content)))
                .findFirst();
            commandExecutor.ifPresent(
                executor -> executor.execute(channel, user, member, parts));
            return;
        }

        // Handle non-regex commands
        String[] temp = content.substring(prefix.length()).split(" ");
        String commandName = temp[0];
        String[] args = new String[] {};

        if(registeredCommands.containsKey(commandName)) {

            // Get arguments if there is any
            if(temp.length > 1) {
                args = Arrays.copyOfRange(temp, 1, temp.length);
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
