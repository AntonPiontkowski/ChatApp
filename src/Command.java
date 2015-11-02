public class Command {
    public CommandType type;

    public Command(CommandType t) {
        this.type = t;
    }

    @Override
    public String toString() {
        if (type.name().equals("DISCONNECT"))
            return "Disconnected";
        else if (type.name().equals("MESSAGE"))
            return "Message";
        else if (type.name().equals("ACCEPT"))
            return "Accepted";
        else if (type.name().equals("REJECT"))
            return "Rejected";
        else
            return "Nick";
    }

    public enum CommandType {
        ACCEPT, DISCONNECT, MESSAGE, NICK, REJECT;
		/*
		 * public CommandType valueOf(String name){ if
		 * (name.equals("Disconnected")) return DISCONNECT; else if
		 * (name.equals("Message")) return MESSAGE; else if
		 * (name.equals("Accepted")) return ACCEPT; else if
		 * (name.equals("Rejected")) return REJECT; else return NICK; }
		 */
    }
}