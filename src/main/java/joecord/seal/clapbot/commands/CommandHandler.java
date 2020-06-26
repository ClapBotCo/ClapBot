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

    public void handleCommand(MessageChannel channel, User user, Member member, Message message){
        String content = message.getContentRaw();
        String[] parts = content.split(" ");
        if(!content.startsWith(prefix)){
            Optional<CommandExecutor> commandExecutor = registeredCommands.values().stream().filter(CommandExecutor::isRegexCommand).filter((executor -> Pattern.matches(executor.getCommand(), content))).findFirst();
            commandExecutor.ifPresent(executor ->   executor.execute(channel, user, member, parts));
            return;
        }
        String commandName = parts[0].substring(1);
        if(registeredCommands.containsKey(commandName)){
            CommandExecutor commandExecutor = registeredCommands.get(commandName);
            if(!commandExecutor.isRegexCommand()) {
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                commandExecutor.execute(channel, user, member, args);
            }
        }
    }
}
