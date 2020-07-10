package joecord.seal.clapbot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;

public class CommandHandler {

    private String prefix;
    /**
     * Maps {@link java.lang.Class#getName()} of subclasses of {@link
     * net.dv8tion.jda.api.events.Event} to {@link 
     * joecord.seal.clapbot.CommandHandler.Entry Entrys} of that same class
     */
    private HashMap<String, Entry<?>> register;    

    private class Entry<E extends Event> {
        private Class<E> eventClass;
        private HashSet<GenericCommand<E>> commands;
        private HashMap<String, GenericCommand<E>> invoked;

        public Entry(Class<E> eventClass) {
            this.eventClass = eventClass;
            this.commands = new HashSet<>();
            this.invoked = new HashMap<>();
        }

        public boolean add(GenericCommand<E> command) {
            boolean success = true;
            if(command.hasProperties(CommandProperty.INVOKED)) {
                for(String alias : command.getAliases()) {
                    if(success) {
                        success = invoked.put(alias, command) == null;
                    }
                    else {
                        invoked.put(alias, command);
                    }
                }
                success = true;
            }
            else {
                success = this.commands.add(command);
            }

            return success;
        }

        public boolean remove(GenericCommand<E> command) {
            boolean success = true;
            if(command.hasProperties(CommandProperty.INVOKED)) {
                for(String alias : command.getAliases()) {
                    if(success) {
                        success = invoked.remove(alias) == null;
                    }
                    else {
                        invoked.remove(alias);
                    }
                }
            }
            else {
                success = this.commands.remove(command);
            }

            return success;
        }

        /**
         * Gets the invoked command registered at the given alias, or null if
         * there is no command registered at this alias
         * @param alias The alias to check
         * @return The (possibly null) command
         */
        public GenericCommand<E> getInvoked(String alias) {
            return invoked.get(alias);
        }

        public HashSet<GenericCommand<E>> getAll() {
            return this.commands;
        }
    }

    /**
     * Construct a new command handler.
     * @param prefix The prefix used for message commands, for example with
     * prefix {@code !}, an invokable command called {@code command} would be
     * called by sending {@code !command} 
     */
    public CommandHandler(String prefix) {
        this.prefix = prefix;
        this.register = new HashMap<>();
    }

    /**
     * Register a GenericCommand.
     * @param <T> Generic type of the event that the command handles
     * @param command The command to register
     * @return True iff the command was not already registered
     */
    public <T extends Event> boolean register(GenericCommand<T> command) {
        @SuppressWarnings("unchecked") // This cast should always work
        Entry<T> entry = (Entry<T>)register.get(command.getEventClass().getName());

        return entry.add(command);
    }

    /**
     * Deregister a GenericCommand.
     * @param <T> Generic type of the event that the command handles
     * @param command The command to deregister
     * @return True iff the command was previously registered
     */
    public <T extends Event> boolean deregister(GenericCommand<T> command) {
        @SuppressWarnings("unchecked") // This cast should always work
        Entry<T> entry = (Entry<T>)register.get(command.getEventClass().getName());

        return entry.remove(command);
    } 

    /**
     * Contains the logic for running commands when a supported JDA event
     * occurs. Contains all logic for handling {@link
     * joecord.seal.clapbot.api.CommandProperty CommandPropertys}.
     * @param <T> Generic type of the event being handled
     * @param eventName The {@link java.lang.Class#getName()} of the event
     * @param event The JDA event that occured
     * @param message If relevant, the raw content string of and relevant
     * message in the event, or null if the event does not have a message
     * @param isBot If relevant, true iff any relevant user in the event is a
     * bot, or false if the event does not concern any users
     */
    public <T extends Event> void onEvent(String eventName, T event, 
        String message, boolean isBot) {

        @SuppressWarnings("unchecked") // This cast should always work
        Entry<T> entry = (Entry<T>)register.get(eventName);

        HashSet<GenericCommand<T>> cmdSet = entry.getAll();

        // Handle CommandProperty.INVOKED
        if(message != null && message.trim().startsWith(this.prefix)) {
            
            String[] parts = message.substring(prefix.length()).split(" ");
            GenericCommand<T> invokedCmd = entry.getInvoked(parts[0]);

            if(invokedCmd != null) {
                cmdSet = new HashSet<GenericCommand<T>>(cmdSet);
                cmdSet.add(invokedCmd);

                // Handle CommandProperty.USES_ARGUMENTS
                if(invokedCmd.hasProperties(CommandProperty.USES_ARGUMENTS)) {
                    invokedCmd.setArguments(
                        Arrays.copyOfRange(parts, 1, parts.length));
                }
            }
        }

        for(GenericCommand<T> cmd : cmdSet) {

            // Handle CommandProperty.RESPECT_BOTS
            if(!cmd.hasProperties(CommandProperty.RESPECT_BOTS) && isBot) {
                // Ignore this event for this command
                continue;
            }

            // Handle CommandProperty.PRIVELAGED
            if(cmd.hasProperties(CommandProperty.PRIVELAGED)) {
                if(!cmd.checkPrivelage(event)) {
                    // If privelage check fails
                    continue;
                }
            }

            // Handle CommandProperty.CONDITIONAL
            if(cmd.hasProperties(CommandProperty.CONDITIONAL)) {
                if(!cmd.checkCondition(event)) {
                    // If condition check fails
                    continue;
                }
            }

            // Handle CommandProperty.META
            if(cmd.hasProperties(CommandProperty.META)) {
                cmd.setCommandHandler(this);
            }

            cmd.execute(event);
        }
    }
    
    public String getPrefix() {
        return this.prefix;
    }

    /* Broken
    public Set<GatewayIntent> getRequiredIntents() {
        EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);

        Set<Class<?>> classSet = new HashSet<>();

        for(String className : this.register.keySet()) {
            classSet.add(Class.forName(className));
        }

        return GatewayIntent.fromEvents(classSet);
    } */
}
