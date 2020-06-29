package joecord.seal.clapbot.commands;

import joecord.seal.clapbot.api.CommandExecutor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandTemplate implements CommandExecutor {

    @Override
    public void execute(MessageChannel channel, User senderUser,
        Member senderMember, String[] arguments) {
        channel.sendMessage("Stub").queue();
    }

    @Override
    public String getCommand() {
        return "Stub";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getUsage() {
        return "Stub";
    }

    @Override
    public boolean isRegexCommand() {
        return false;
    }
}
