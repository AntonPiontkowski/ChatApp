public class NickCommand extends Command{
    public boolean busy;
    public String version;
    public String nick;
    public NickCommand(String version,String nick,boolean busy){
        this.version = version;
        this.nick = nick;
        this.busy = busy;
    }
}
