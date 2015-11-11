import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketAddress;
import java.util.Observable;
import java.util.Scanner;

public class CallListenerThread extends Observable implements Runnable {
    private CallListener callListener;
    private Connection lastRequest;
    private static final String VER = "ChatApp 2015";

    public CallListenerThread() {
    }

    public CallListenerThread(CallListener callListener) {
        this.callListener = callListener;
    }

    public SocketAddress getListenAddress() {
        return this.callListener.getListenAddress();
    }

    public String getLocalNick() {
        return this.callListener.getLocalNick();
    }

    public SocketAddress getRemoteAddress() {
        return this.callListener.getRemoteAddress();
    }

    public String getRemoteNick() {
        return this.callListener.getRemoteNick();
    }

    public boolean isBusy() {
        return this.callListener.isBusy();
    }

    public void setBusy(boolean busy) {
        this.callListener.setBusy(busy);
    }

    public void setListenAddress(SocketAddress listenAddress) {
        this.callListener.setListenAddress(listenAddress);
    }

    public void setLocalNick(String localNick) {
        this.callListener.setLocalNick(localNick);
    }

    public Connection getLastRequest() {
        return this.lastRequest;
    }

    @Override
    public void run() {
        while (true) {
            if (isBusy() != true) {
                lastRequest = callListener.getConnection();
                setBusy(true);
                setChanged();
                notifyObservers();
                clearChanged();
            }
        }
    }

}
