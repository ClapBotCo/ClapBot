package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Clapbot's event listener. Calls the command handler's {@link
 * joecord.seal.clapbot.CommandHandler#onEvent CommandHandler#onEvent} method
 * whenever a supported {@link net.dv8tion.jda.api.events.GenericEvent JDA 
 * GenericEvent} is received.
 * 
 * <h3>Supported Events</h3>
 * <p><ul>
 * <li>GuildMessageReceivedEvent
 * <li>GuildMemberJoinEvent
 * <li>GuildMessageReactionAddEvent
 * </ul><p>
 */
public class Listener extends ListenerAdapter {
    private CommandHandler commandHandler;

    public Listener(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        this.commandHandler.onEvent(
            GuildMessageReceivedEvent.class,
            event,
            event.getMessage().getContentRaw(),
            event.getAuthor().isBot()
        );
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        this.commandHandler.onEvent(
            GuildMemberJoinEvent.class,
            event,
            null,
            event.getUser().isBot()
        );
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        this.commandHandler.onEvent(
            GuildMessageReactionAddEvent.class,
            event,
            null,
            event.getUser().isBot()
        );
    }


}