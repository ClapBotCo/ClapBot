package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import joecord.seal.clapbot.api.CommandProperty;
import joecord.seal.clapbot.api.GenericCommand;

public class CommandHandler {

    /**
     * The string used before a command alias for all commands with the
     * {@link joecord.seal.clapbot.api.CommandProperty#INVOKED invoked}
     * property.
     */
    private String prefix;
    /**
     * Maps {@link java.lang.Class} of subclasses of {@link
     * net.dv8tion.jda.api.events.GenericEvent JDA GenericEvent} to {@link 
     * joecord.seal.clapbot.CommandHandler.Entry Entrys} of that same class.
     */
    private HashMap<Class<? extends GenericEvent>, Entry<?>> register;    

    /**
     * Construct a new command handler.
     * @param prefix The prefix used for commands, for example with
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
    public <T extends GenericEvent> boolean register(GenericCommand<T> command) {
        @SuppressWarnings("unchecked") // This cast should always work
        Entry<T> entry = (Entry<T>)register.get(command.getEventClass());

        if(entry == null) {
            // First of it's kind to get registered
            entry = new Entry<T>(command.getEventClass());
            this.register.put(command.getEventClass(), entry);
        }

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
        Entry<T> entry = (Entry<T>)register.get(command.getEventClass());

        if(entry == null) {
            // No commands registered at this event class
            return false;
        }

        return entry.remove(command);
    } 

    /**
     * Contains the logic for running commands when a supported JDA event
     * occurs. Contains all logic for handling {@link
     * joecord.seal.clapbot.api.CommandProperty CommandPropertys}.
     * 
     * @param <T> Generic type of the event being handled
     * @param eventClass The {@link java.lang.Class} of the event
     * @param event The JDA event that occured
     * @param message If relevant, the raw content string of and relevant
     * message in the event, or null if the event does not have a message
     * @param isBot If relevant, true iff any relevant user in the event is a
     * bot, or false if the event does not concern any users
     */
    public <T extends GenericEvent> void onEvent(Class<T> eventClass, T event, 
        String message, boolean isBot) {

        @SuppressWarnings("unchecked") // This cast should always work
        Entry<T> entry = (Entry<T>)register.get(eventClass);

        if(entry == null) {
            // No commands registered for this event
            return;
        }

        // Set of commands to iterate over
        Set<GenericCommand<T>> cmdSet = entry.getAll();

        // Handle CommandProperty.INVOKED
        if(message != null && message.trim().startsWith(this.prefix)) {
            
            String[] parts = message.substring(prefix.length()).split(" ");
            GenericCommand<T> invokedCmd = entry.getInvoked(parts[0]);

            if(invokedCmd != null) {
                // If we found a new command, add it to the set by making a copy
                // of the set and adding it.
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

            // Beware, this code contains the evil continue statement! :O

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
                // Give the command the command handler that it needs
                cmd.setCommandHandler(this);
            }

            cmd.execute(event);
        }
    }
    
    /**
     * Get the configured prefix. The prefix is the string used before a
     * command alias for all commands with the invoked property.
     * @return Prefix string
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Parse the required {@link net.dv8tion.jda.api.requests.GatewayIntent 
     * GatewayIntents} from the commands registered.
     * @return Set of required GatewayIntents
     */
    public Set<GatewayIntent> getRequiredIntents() {
        return GatewayIntent.fromEvents(this.register.keySet());
    }

    /**
     * A collection of registered commands, each Entry corresponds to a
     * specific subclass of {@link
     * net.dv8tion.jda.api.events.GenericEvent GenericEvent} and all commands
     * in the entry use the same GenericEvent subclass as their event class.
     * @param <E> Generic type of the GenericEvent subclass for this entry
     */
    public class Entry<E extends GenericEvent> {
        private HashSet<GenericCommand<E>> commands;
        private HashMap<String, GenericCommand<E>> invoked;

        public Entry(Class<E> eventClass) {
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

        /**
         * Gets all registered non-invoked commands.
         * @return Set of commands
         */
        public Set<GenericCommand<E>> getAll() {
            return this.commands;
        }

        /* Rudimentary toString
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder(32);
            str.append("[");
            for(GenericCommand<E> cmd : this.commands) {
                str.append(cmd.getDisplayName() + ", ");
            }
            for(String s : this.invoked.keySet()) {
                str.append(this.invoked.get(s).getDisplayName() + ", ");
            }
            str.append("]");

            Class<?> d = CommandHandler.class;
            d.hashCode();

            return str.toString();
        } */
    }
}
