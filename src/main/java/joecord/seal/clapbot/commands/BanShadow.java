package joecord.seal.clapbot.commands;

import joecord.seal.clapbot.api.CommandExecutor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class BanShadow implements CommandExecutor {

    @Override
    public void execute(MessageChannel channel, User senderUser, Member senderMember, String[] arguments) {
        double num = Math.random();
        String msg = ((num < 0.5) ? "#ModShadow" : "#BanShadow");
        channel.sendMessage(msg).queue();
    }

    @Override
    public String getCommand() {
        return ".*shadow.*";
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
