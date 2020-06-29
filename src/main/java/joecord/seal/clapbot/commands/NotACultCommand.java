package joecord.seal.clapbot.commands;

import joecord.seal.clapbot.api.CommandExecutor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class NotACultCommand implements CommandExecutor {

    @Override
    public void execute(MessageChannel channel, User senderUser, Member senderMember, String[] arguments) {
        channel.sendMessage(":joeL: " + senderMember.getEffectiveName() + " has said a forbidden word").queue();
    }

    @Override
    public String getCommand() {
        return "cult";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public boolean isRegexCommand() {
        return true;
    }
}
