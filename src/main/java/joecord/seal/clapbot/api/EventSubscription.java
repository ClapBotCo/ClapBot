package joecord.seal.clapbot.api;

import java.util.HashMap;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public enum EventSubscription {
    
    MESSAGE_RECIEVED_EVENT(MessageReceivedEvent.class),
    
    GUILD_MEMBER_JOIN_EVENT(GuildMemberJoinEvent.class),
    
    GUILD_MESSAGE_REACTION_ADD_EVENT(GuildMessageReactionAddEvent.class);

    private static final HashMap<EventSubscription, Class<? extends Event>> VALUE_MAP = new HashMap<>();

    private final Class<? extends Event> eventClass;

    static {
        for(EventSubscription e : values()) {
            VALUE_MAP.put(e, e.eventClass);
        }
    }

    private EventSubscription(Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
    }

}