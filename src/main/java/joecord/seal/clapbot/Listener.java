package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
    private CommandHandler commandHandler;

    public Listener(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        commandHandler.onEvent(event.getClass(), event);
    }
}