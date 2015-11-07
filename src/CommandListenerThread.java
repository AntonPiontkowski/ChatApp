import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

public class CommandListenerThread extends Observable implements Runnable {
    private Connection connection;
    private boolean disconnected;
    private Command lastCommand;
    private MessageCommand lastMessageCommand;
    private NickCommand lastNickCommand;

    public CommandListenerThread() {
    }

    public CommandListenerThread(Connection con) {
        this.connection = con;
        this.lastCommand = new Command();
        this.lastMessageCommand = new MessageCommand();
        this.lastNickCommand = new NickCommand();
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.lastCommand = connection.receive();
                switch (lastCommand.type){
                    // TODO HANDLE ALL THE POSSIBLE VARIANTS
                    case ACCEPT:{
                        this.disconnected = false;
                        break;
                    }
                    case DISCONNECT:{
                        this.connection.close();
                        this.disconnected = true;
                        break;
                    }
                    case MESSAGE:{
                        this.lastMessageCommand.message = connection.receiveMessage();
                        break;
                    }
                    case NICK:{
                        String[] info = this.connection.receiveNickVer();
                        if (info != null){
                            this.lastNickCommand.version = info[0];
                            this.lastNickCommand.nick = info[1];
                            this.disconnected = false;
                        }
                        else if (info.length == 5){
                            this.lastMessageCommand.message = Caller.CallStatus.BUSY.name();
                            this.connection.close();
                            this.disconnected = true;
                        }
                        else {
                            this.connection.reject();
                            this.disconnected = true;
                        }
                        break;
                    }
                    case REJECT:{
                        this.disconnected = true;
                        break;
                    }
                    default:{
                        this.connection.reject();
                        this.disconnected = true;
                        break;
                    }
                }
                this.setChanged();
                this.notifyObservers();
                this.clearChanged();
            }
            catch (IOException e) {
                // TODO HANDLE EXCEPTION
                this.disconnected = true;
            }
            catch (NoSuchElementException e2){
                // TODO HANDLE EXCEPTION
                this.disconnected = true;
            }
        }
    }
    public Command.CommandType getLastCommand() {
        return this.lastCommand.type;
    }

    public String getMessage() {
        return this.lastMessageCommand.message;
    }

    public String getNick() {
        return this.lastNickCommand.nick;
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public void start() {
        // TODO IF THERE WILL BE A NEED
    }

    public void stop() throws InterruptedException{
        // TODO IF THERE WILL BE A NEED
    }

    public static void main(String[] args) {
    }
}
