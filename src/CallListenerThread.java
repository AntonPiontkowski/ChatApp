import java.net.SocketAddress;
import java.util.Observable;

public class CallListenerThread extends Observable implements Runnable{
    private CallListener callListener;

    public CallListenerThread(){}
    public CallListenerThread(CallListener callListener){
        this.callListener = callListener;
    }

    public SocketAddress getListenAddress(){
        return this.callListener.getListenAddress();
    }
    public String getLocalNick(){
        return this.callListener.getLocalNick();
    }
    public SocketAddress getRemoteAddress(){
        return this.callListener.getRemoteAddress();
    }
    public String getRemoteNick(){
        return this.callListener.getRemoteNick();
    }
    public boolean isBusy(){
        return this.callListener.isBusy();
    }
    public void setBusy(boolean busy){
        this.callListener.setBusy(busy);
    }
    public void setListenAddress(SocketAddress listenAddress){
        this.callListener.setListenAddress(listenAddress);
    }
    public void setLocalNick(String localNick){
        this.callListener.setLocalNick(localNick);
    }

    @Override
    public void run() {}
    public static void main(String[] args){}
}
