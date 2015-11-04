import java.io.IOException;
import java.util.Observable;

public class CommandListenerThread extends Observable implements Runnable {
    private Connection connection;
    private boolean disconnected;
    private Command lastCommand;

    public CommandListenerThread(){
    }
    public CommandListenerThread(Connection con){
        this.connection = con;
    }
    @Override
    public void run() {
        while (true){
            try{
                this.lastCommand = connection.receive();
            } catch (IOException e){
                //HANDLE EXCEPTION
            }
        }
    }
    public static void main(String[] args){}
}
