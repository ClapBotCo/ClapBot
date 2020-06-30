package joecord.seal.clapbot.commands.conditional_commands;

import java.util.regex.Pattern;

import joecord.seal.clapbot.commands.ConditionalCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GnEmanCommand extends ConditionalCommand {

    public GnEmanCommand(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void execute() {
        channel.sendMessage("gn " + getMember().getEffectiveName()
            .toLowerCase().replaceAll("quirky", "")).queue();

        /* Alternative that only responds to eman
        if(senderUser.getId().equals("280177678282129409")) {
            // If sent by eman
            channel.sendMessage("gn eman").queue();
        }
        */
    }

    @Override
    public String getName() {
        return "gn eman";
    }

    @Override
    public boolean check() {
        return Pattern.matches(".*gn modmail.*", getMessage().getContentRaw());
    }

    @Override
    public String getDescription() {
        return "Says gn to anyone that says 'gn modmail' :)";
    }
}
