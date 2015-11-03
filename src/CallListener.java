import java.io.IOException;
import java.net.SocketAddress;
import java.net.Socket;
import java. net.ServerSocket;

public class CallListener {
    private boolean busy;
    private SocketAddress listenAddress;
    private String localNick;
    private SocketAddress remoteAddress;
    private String remoteNick;
    Socket incoming;
    Connection connection;

    public CallListener(){}
    public CallListener(String localNick){
        this.localNick = localNick;
    }
    public CallListener(String localNick,String localIp){}

    public Connection getConnection(){
        if (busy == false){
            try {
                ServerSocket server = new ServerSocket(28411);
                incoming = server.accept();
                connection = new Connection(incoming);
            }catch (IOException e){}
            connection.accept();
            return connection;
        }else {
            connection.sendNickBusy("ChatApp 2015", localNick);
            return null;
        }
    }
    public SocketAddress getListenAddress(){
        return this.listenAddress;
    }
    public String getLocalNick(){
        return this.localNick;
    }
    public SocketAddress getRemoteAddress(){
        return this.remoteAddress;
    }
    public String getRemoteNick(){
        return this.remoteNick;
    }
    public boolean isBusy(){
        return this.busy;
    }
    public void setBusy(boolean busy){
        this.busy = busy;
    }
    public void setLocalNick(String localNick){
        this.localNick = localNick;
    }
    public void setListenAddress(SocketAddress listenAddress){
        this.listenAddress = listenAddress;
    }
    public static void main(String[] args){}
}
