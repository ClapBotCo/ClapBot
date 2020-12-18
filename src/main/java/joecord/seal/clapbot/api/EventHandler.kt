package joecord.seal.clapbot.api

import net.dv8tion.jda.api.events.GenericEvent

abstract class EventHandler<E : GenericEvent> {
    abstract fun handle(event: E)
}