package joecord.seal.clapbot.commands.message;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Embed {

    public static EmbedBuilder eb = new EmbedBuilder();

    public Embed(String title, String text, String footer) {
       eb.setColor(new Color(0xff7b00));
       eb.setTitle(title);
       eb.setFooter(footer);
       eb.addField("title", text, false);
    }
}
