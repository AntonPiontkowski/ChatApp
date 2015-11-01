import java.util.Observable;

public class CommandListenerThread extends Observable implements Runnable {
    private Connection connection;
    private boolean disconnected;
    private Command lastCommand;

    public CommandListenerThread(){}
    public CommandListenerThread(Connection con){
        this.connection = con;
    }
    @Override
    public void run() {}
    public static void main(String[] args){}
}
