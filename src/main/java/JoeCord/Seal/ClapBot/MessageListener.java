package JoeCord.Seal.ClapBot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.EventListener;

public class MessageListener implements EventListener {
    public void MessageListener(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        System.out.println(event.getChannel()
                + event.getAuthor().getName() + ":"
                + event.getMessage().getContentRaw());
        new CommandHandler(message);
    }
}
