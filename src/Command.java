public class Command {
    public CommandType type;

    public Command(CommandType t) {
        this.type = t;
    }
    public Command(){};
    /*
    * WILL BE EDITED
    */
   /* @Override
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
    }*/

    public enum CommandType {
        ACCEPT, DISCONNECT, MESSAGE, NICK, REJECT;

    }
}