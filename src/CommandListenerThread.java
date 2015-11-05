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
    public Command getLastCommand(){
        return this.lastCommand;
    }
    public boolean isDisconnected(){
        return this.disconnected;
    }
    public void start(){
        // ADD
    }
    public void stop(){
        // ADD
    }
    public static void main(String[] args){}
}
