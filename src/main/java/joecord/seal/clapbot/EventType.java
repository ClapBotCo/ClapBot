package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public enum EventType {
    MESSAGE_RECEIVED,

    MEMBER_JOIN,

    REACTION_ADD;

    public Class<? extends Event> getEventClass() {
        switch(this) {
            case MESSAGE_RECEIVED:
                return MessageReceivedEvent.class;
            case MEMBER_JOIN:
                return GuildMemberJoinEvent.class;
            case REACTION_ADD:
                return GuildMessageReactionAddEvent.class;
            default:
                throw new IllegalStateException("Enum value not set");
        }
    }

    
    
}