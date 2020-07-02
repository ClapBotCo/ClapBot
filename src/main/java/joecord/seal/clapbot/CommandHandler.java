package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import joecord.seal.clapbot.commands.memberJoin.AbstractMemberJoinCommand;
import joecord.seal.clapbot.commands.conditional.AbstractConditionalCommand;
import joecord.seal.clapbot.commands.message.AbstractMessageCommand;
import joecord.seal.clapbot.commands.reactionAdd.AbstractReactionAddCommand;

public class CommandHandler extends ListenerAdapter {

    private String prefix;
    private HashMap<String, AbstractMessageCommand> messageCommands;
    private HashSet<AbstractConditionalCommand> conditionalCommands;
    private HashSet<AbstractMemberJoinCommand> memberJoinCommands;
    private HashSet<AbstractReactionAddCommand> reactionAddCommands;

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
        this.memberJoinCommands = new HashSet<>();
        this.reactionAddCommands = new HashSet<>();
    }

    public List<GatewayIntent> getIntents() {
        LinkedList<GatewayIntent> intents = new LinkedList<>();

        if(!this.messageCommands.isEmpty()) {
            intents.add(GatewayIntent.GUILD_MESSAGES);
        }
        if(!this.memberJoinCommands.isEmpty()) {
            intents.add(GatewayIntent.GUILD_MEMBERS);
        }
        if(!this.reactionAddCommands.isEmpty()) {
            intents.add(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        }

        return intents;
    }

    /**
     * Register a message command
     * @param command The command to register
     */
    public void register(AbstractMessageCommand command) {
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
    public void register(AbstractConditionalCommand command) {
        conditionalCommands.add(command);
    }

    /**
     * Register a member join command
     * @param command The command to register
     */
    public void register(AbstractMemberJoinCommand command) {
        memberJoinCommands.add(command);
    }

    /**
     * Register a reaction add command
     * @param command The command to register
     */
    public void register(AbstractReactionAddCommand command) {
        reactionAddCommands.add(command);
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
            for(AbstractConditionalCommand command : conditionalCommands) {
                if(command.check(event)) {
                    command.execute(event);
                }
            }
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        // Logging
        System.out.println(event.getUser().getName() + " just joined");

        for(AbstractMemberJoinCommand command : memberJoinCommands) {
            command.execute(event);
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        // Logging
        System.out.println(event.getUser().getName() + " just reacted " +
            event.getReactionEmote().getName() + " to message id " + 
            event.getMessageId());

            for(AbstractReactionAddCommand command : reactionAddCommands) {
                command.execute(event);
            }
    }
}
