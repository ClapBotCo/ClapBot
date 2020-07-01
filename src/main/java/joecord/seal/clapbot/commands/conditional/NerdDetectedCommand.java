package joecord.seal.clapbot.commands.conditional;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NerdDetectedCommand extends AbstractConditionalCommand {

    /** Triggers when the counter reaches this number */
    private static final int COUNTER_MAX = 4;
    /** Counter is reset to zero if no nerd messages are sent in this amount
     * of time */
    private static final long TIMER_MILLISECONDS = 120000;

    /** A counter to keep track of the number of nerd words said before
     * intervening */
    private int counter;
    /** A timer to reset the counter to zero if no nerd messages are sent 
     * within TIMER_MILLISECONDS milliseconds */
    private Timer resetTimer;
    /** True iff the timer is currently running */
    private boolean timerActive;

    public NerdDetectedCommand() {
        this.name = "Nerd detected";
        this.description = String.format((
            "Reminds Alec & Nick to keep the nerd chat to #nerdclaps if " + 
            "they send %d or more nerdy messages in the span of %d minutes"),
             COUNTER_MAX, (TIMER_MILLISECONDS / 60000));
        this.counter = 0;
        this.resetTimer = new Timer();
        this.timerActive = false;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        MessageBuilder msg = new MessageBuilder();
        IMentionable nerdClapsMention =
            event.getGuild().getTextChannelById("643815565533642773");

        msg.append(event.getAuthor().getAsMention()); // @Nick / @Alec
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
        boolean counterExceeded = false;
        String[] nerdWords = new String[] {
            "java", "sql", "o(", "pointer", "int", "osi", "tcp", "ip", "layer",
            "c89", "py", "git", "port", "interface", "class", "method",
            "time complexity", "dsa", "ucp", "oopd", "oose", "fop", "mad",
            "ptd", "os", "network", "dave", "mark", "antoni", "wanquan",
            "stack", "memory"};

        // Make sure it's sent by Alec or Nick and not in already in nerdclaps
        if(!event.getTextChannel().getId().equals("643815565533642773") &&
            (event.getAuthor().getId().equals("175851085456474113") ||
            event.getAuthor().getId().equals("297978513250713602"))) {

            for(String word : nerdWords) {
                if(event.getMessage().getContentRaw().toLowerCase()
                    .contains(word)) {
                    
                    counter++;
                    if(counter >= COUNTER_MAX) {
                        counter = 0;
                        counterExceeded = true;
                        if(timerActive) {
                            this.resetTimer.cancel();
                            timerActive = false;
                        }
                        break;
                    }

                    /* Restart the timer, this resets the counter to zero if
                     * no nerd messages are sent in a TIMER_MILLISECONDS
                     * window */
                    if(timerActive) {
                        this.resetTimer.cancel();
                        timerActive = false;
                    }
                    this.resetTimer = new Timer();
                    this.resetTimer.schedule(
                        new TimerTask(){ 
                            @Override public void run() {
                                counter = 0;
                                timerActive = false;
                            }
                        },
                        TIMER_MILLISECONDS);
                    timerActive = true;
                }
            }
        }

        return counterExceeded;
    }
}