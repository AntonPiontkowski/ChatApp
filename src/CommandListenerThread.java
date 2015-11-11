import javax.swing.*;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

public class CommandListenerThread extends Observable implements Runnable {
    private volatile Connection connection;
    private volatile boolean disconnected;
    private volatile Command lastCommand;
    private Thread t;

    public CommandListenerThread() {
    }
 
    public CommandListenerThread(Connection con) {
        this.connection = con;
    }

    @Override
    public void run() {
        while (!isDisconnected()) {
            try {
                /*
                this.lastCommand can be an instance of Command, MessageCommand or NickCommand
                 */
                this.lastCommand = connection.receive();
                if (this.lastCommand != null){
                    if (this.lastCommand instanceof MessageCommand){
                        ((MessageCommand) this.lastCommand).message = "EMPTY";
                        ((MessageCommand) this.lastCommand).message = this.connection.receiveMessage();
                    } else if (this.lastCommand instanceof NickCommand){
                        String[] remoteInfo = this.connection.receiveNickVer(this.connection.getCommandText());
                        ((NickCommand)this.lastCommand).nick = remoteInfo[1];
                        ((NickCommand)this.lastCommand).version = remoteInfo[0];
                        if (remoteInfo.length == 3)
                            ((NickCommand)this.lastCommand).busy = true;
                        else ((NickCommand)this.lastCommand).busy = false;
                    } else{
                        switch (lastCommand.type) {
                            // TODO HANDLE ALL THE POSSIBLE VARIANTS
                            case ACCEPT: {
                                this.disconnected = false;
                                break;
                            }
                            case DISCONNECT: {
                                this.connection.close();
                                this.disconnected = true;
                                break;
                            }
                            case REJECT: {
                                this.disconnected = true;
                                break;
                            }
                            default: {
                                this.connection.reject();
                                this.disconnected = true;
                                break;
                            }
                        }
                    }
                    this.setChanged();
                    this.notifyObservers();
                    this.clearChanged();
                }
            } catch (IOException e) {
                // TODO HANDLE EXCEPTION
                e.printStackTrace();
                this.disconnected = true;
            } catch (NoSuchElementException e2) {
                // TODO HANDLE EXCEPTION
                this.disconnected = true;
                e2.printStackTrace();
            }
        }
    }

    public Command getLastCommand() {
        return this.lastCommand;
    }


    public String getMessage() {
        return ((MessageCommand)this.lastCommand).message;
    }

    public String getNick() {
        return ((NickCommand)this.lastCommand).nick;
    }
    public String getVer(){
        return ((NickCommand)this.lastCommand).version;
    }
    public boolean isBusy(){
        return ((NickCommand)this.lastCommand).busy;
    }
    public boolean isDisconnected() {
        return this.disconnected;
    }

    public void start() {
        this.disconnected = false;
        this.t = new Thread(this);
        t.start();
    }

    public void stop() throws InterruptedException {
        this.disconnected = true;
    }

}
