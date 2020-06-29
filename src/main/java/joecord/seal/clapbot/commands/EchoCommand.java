package joecord.seal.clapbot.commands;

import joecord.seal.clapbot.api.CommandExecutor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class EchoCommand implements CommandExecutor {

    @Override
    public void execute(MessageChannel channel, User senderUser, Member senderMember, String[] arguments) {
        channel.sendMessage(String.join(" ", arguments)).queue();
    }

    @Override
    public String getCommand() {
        return "echo";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getUsage() {
        return "<message to echo>";
    }

    @Override
    public boolean isRegexCommand() {
        return false;
    }
}
