package joecord.seal.clapbot.commands;

import joecord.seal.clapbot.api.CommandExecutor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class GnEmanCommand implements CommandExecutor {

    @Override
    public void execute(MessageChannel channel, User senderUser,
        Member senderMember, String[] arguments) {
        /*
        if(senderUser.getId().equals("280177678282129409")) {
            // If sent by eman
            channel.sendMessage("gn eman").queue();
        }
        */
        channel.sendMessage("gn " + senderMember.getEffectiveName()
            .toLowerCase().replaceAll("quirky", "")).queue();
    }

    @Override
    public String getCommand() {
        return ".*gn modmail.*";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getUsage() {
        return "'gn modmail'";
    }

    @Override
    public boolean isRegexCommand() {
        return true;
    }
}
