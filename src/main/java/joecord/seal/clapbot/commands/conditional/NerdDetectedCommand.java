package joecord.seal.clapbot.commands.conditional;

import java.util.regex.Pattern;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NerdDetectedCommand extends ConditionalCommand {

    private int counter;

    public NerdDetectedCommand() {
        super(
            "Nerd detected",
            "Reminds Alec & Nick to keep the nerd chat to #nerdclaps"
        );
        counter = 0;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageBuilder msg = new MessageBuilder();
        IMentionable nerdClapsMention =
            event.getGuild().getTextChannelById("643815565533642773");

        msg.append(event.getAuthor().getAsMention());
        msg.append(" Nerd detected! Get back to ");
        if(nerdClapsMention != null) { // If we're in joecord
            msg.append(nerdClapsMention);
        }
        else { // If we aren't in joecord so cant mention nerdclaps
            msg.append("nerdclaps");
        }

        event.getChannel().sendMessage(msg.build()).queue();
    }

    @Override
    public boolean check(MessageReceivedEvent event) {
        StringBuilder regex = new StringBuilder(".*(");
        String[] nerdWords = new String[] {"nerd"};
        boolean counterReset = false;

        // Make sure it's sent by Alec or Nick and not in nerdclaps
        if(!event.getTextChannel().getId().equals("643815565533642773") &&
            (event.getAuthor().getId().equals("175851085456474113") ||
            event.getAuthor().getId().equals("297978513250713602"))) {

            regex.append(nerdWords[0]);
            for(int i = 1; i < nerdWords.length; i++)
            for(String word : nerdWords) {
                regex.append("|");
                regex.append(word);
            }
            regex.append(").*");

            if(Pattern.matches(regex.toString(), 
                event.getMessage().getContentRaw().toLowerCase())) {
                counter++;
                if(counter >= 3) {
                    counterReset = true;
                    counter = 0;
                }
            }
        }

        return counterReset;
    }
}
