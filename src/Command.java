public class Command{
    public CommandType type;
    public Command(CommandType t){
        this.type = t;
    }
    public enum CommandType{
    ACCEPT,DISCONNECT,MESSAGE,NICK,REJECT;
}
}