package JoeCord.Seal.ClapBot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.EventListener;

public class MessageListener extends ListenerAdapter
{
    MessageListener() {
        System.out.println("Checkpoint1");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.print (event.getChannel().getName() + " - "
                + event.getAuthor().getName() + ": "
                + event.getMessage().getContentRaw() + "\n");

        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = message.getContentRaw();

        String[] array = content.split(" ");
        try {
            if (array[0].charAt(0) == '!') {
                System.out.println("Command detected!");
                new CommandHandler(event.getChannel(), array);
            }
        }
        catch (Exception exception) {
            System.out.println("Picture or empty message detected!");
        }
    }
}
