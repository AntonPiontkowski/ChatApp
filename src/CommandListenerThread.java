import java.io.IOException;
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
                        else{
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
                super.setChanged();
                super.notifyObservers();
                super.clearChanged();
            } catch (IOException e) {
                // TODO HANDLE EXCEPTION
            }
        }
    }

    public Command getLastCommand() {
        return this.lastCommand;
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

    @Override
    public synchronized void addObserver(Observer o) {
    }

    public void start() {
        // TODO
    }

    public void stop() throws InterruptedException{
        // TODO
    }

    public static void main(String[] args) {
    }
}
