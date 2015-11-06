public class Command {
    public CommandType type;

    public Command(CommandType t) {
        this.type = t;
    }

    public Command() {
    }

    ;

    public enum CommandType {
        ACCEPT, DISCONNECT, MESSAGE, NICK, REJECT;

    }
}