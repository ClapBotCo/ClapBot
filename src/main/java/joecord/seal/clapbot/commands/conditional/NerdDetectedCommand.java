package joecord.seal.clapbot.commands.conditional;

import java.util.Timer;
import java.util.TimerTask;

import joecord.seal.clapbot.api.legacy.LegacyCommandProperty;
import joecord.seal.clapbot.api.legacy.LegacyGenericCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class NerdDetectedCommand extends LegacyGenericCommand<MessageReceivedEvent> {

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
        super(MessageReceivedEvent.class,
            LegacyCommandProperty.CONDITIONAL, LegacyCommandProperty.PRIVILEGED);

        this.displayName = "Nerd detected";
        this.description = String.format((
            "Reminds Alec & Nick to keep the nerd chat to #nerdclaps if " +
            "they send too many nerd messages"),
             COUNTER_MAX, (TIMER_MILLISECONDS / 60000));
        this.counter = 0;
        this.resetTimer = new Timer();
        this.timerActive = false;

        super.setConditionDesc(String.format(
            "Message must be one of %d messages sent outside of #nerdclaps " +
            "within the last %d minutes that has contained a nerd word",
            COUNTER_MAX, (TIMER_MILLISECONDS / 60000)));
        super.setCondition(event -> this.check(event));
        super.setPrivilegeDesc("Only triggers on Alec or Nick");
        super.setPrivilege(event ->
            event.getAuthor().getIdLong() == 175851085456474113l ||
                event.getAuthor().getIdLong() == 297978513250713602l);
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

    /**
     * Private method to put condition logic rather than clogging up the
     * constructor.
     * @param event The MessageReceivedEvent that the command concerns
     * @return Tru
     */
    private boolean check(MessageReceivedEvent event) {
        boolean counterExceeded = false;
            String[] nerdWords = new String[] {
                "java", "sql", "o(", "pointer", "int", "osi", "tcp", "ip",
                "layer", "c89", "py", "git", "port", "interface", "class",
                "method", "time complexity", "dsa", "ucp", "oopd", "oose",
                "fop", "mad", "ptd", "os", "network", "dave", "mark", "antoni",
                "wanquan", "stack", "memory"};

            // Make sure it's not in already in nerdclaps
            if(event.getTextChannel().getIdLong() == 643815565533642773l) {
                return false;
            }

            for(String word : nerdWords) {
                if(event.getMessage().getContentRaw().toLowerCase()
                    .contains(word)) {
                    
                    counter++;
                    if(counter >= COUNTER_MAX) {
                        counter = 0;
                        counterExceeded = true;
                        if(this.timerActive) {
                            this.resetTimer.cancel();
                            this.timerActive = false;
                        }
                        break;
                    }

                    /* Restart the timer, this resets the counter to zero if
                    * no nerd messages are sent in a TIMER_MILLISECONDS
                    * window */
                    if(this.timerActive) {
                        this.resetTimer.cancel();
                        this.timerActive = false;
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

            return counterExceeded;
    }
}