package joecord.seal.clapbot;

import joecord.seal.clapbot.commands.CommandHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter
{
    private CommandHandler commandHandler;

    public Listener(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.print (event.getChannel().getName() + " - "
                + event.getAuthor().getName() + ": "
                + event.getMessage().getContentRaw() + "\n");

        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        try {
            this.commandHandler.handleCommand(
                event.getChannel(),
                event.getAuthor(),
                event.getMember(),
                message);
        }
        catch (Exception exception) {
            System.out.println("Picture or empty message detected!");
            exception.printStackTrace();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println(event.getUser().getName() + " has just joined " +
            event.getGuild().getName());
    }
}
