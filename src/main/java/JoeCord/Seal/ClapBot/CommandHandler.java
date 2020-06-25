package JoeCord.Seal.ClapBot;

public class CommandHandler {
    CommandHandler(String message) {
        message.toLowerCase();
        if (message.contains("clap")) {
            System.out.println("Test");
        }
    }
}
