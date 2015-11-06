import java.io.IOException;
import java.util.Observable;

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
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.lastCommand = connection.receive();
                switch (lastCommand.type){
                    // TODO HANDLE ALL THE POSSIBLE VARIANTS
                    case ACCEPT:{

                    }
                    case DISCONNECT:{

                    }
                    case MESSAGE:{

                    }
                    case NICK:{

                    }
                    case REJECT:{

                    }
                    default:{

                    }
                }
                this.setChanged();
                this.notifyObservers();
                this.clearChanged();
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

    public void start() {
        // TODO
    }

    public void stop() throws InterruptedException{
        // TODO
    }

    public static void main(String[] args) {
    }
}
