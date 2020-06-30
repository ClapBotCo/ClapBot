package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import joecord.seal.clapbot.commands.memberJoin.MemberJoinCommand;
import joecord.seal.clapbot.commands.conditional.ConditionalCommand;
import joecord.seal.clapbot.commands.message.MessageCommand;

public class CommandHandler extends ListenerAdapter {

    private String prefix;
    private HashMap<String, MessageCommand> messageCommands;
    private HashSet<ConditionalCommand> conditionalCommands;
    private HashSet<MemberJoinCommand> memberJoinCommands;

    /**
     * Construct a new command handler.
     * @param prefix The prefix used for message commands, for example with
     * prefix {@code !}, a command called {@code command} would be called by
     * sending {@code !command} 
     */
    public CommandHandler(String prefix){
        this.prefix = prefix;
        this.messageCommands = new HashMap<>();
        this.conditionalCommands = new HashSet<>();
    }

    /**
     * Register a message command
     * @param command The command to register
     */
    public void register(MessageCommand command) {
        if(!messageCommands.containsKey(command.getName())) {
            messageCommands.put(command.getName(), command);

            String[] aliases = command.getAliases();

            if(aliases.length != 0) {
                for(String alias : aliases) {
                    if(!messageCommands.containsKey(alias)) {
                        messageCommands.put(alias, command);
                    }
                }
            }
        }
    }

    /**
     * Register a conditional command
     * @param command The command to register
     */
    public void register(ConditionalCommand command) {
        if(!conditionalCommands.contains(command)) {
            conditionalCommands.add(command);
        }
    }

    /**
     * Register a member join command
     * @param command The command to register
     */
    public void register(MemberJoinCommand command) {
        if(!memberJoinCommands.contains(command)) {
            memberJoinCommands.add(command);
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

                // Get the command and execute it
                messageCommands.get(commandName).execute(event, args);
            }
        }
        else { // Handle conditional commands
            for(ConditionalCommand command : conditionalCommands) {
                if(command.check(event)) {
                    command.execute(event);
                }
            }
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        // Logging
        System.out.print(event.getUser().getName() + " just joined");

        for(MemberJoinCommand command : memberJoinCommands) {
            command.execute(event);
        }
    }
}
