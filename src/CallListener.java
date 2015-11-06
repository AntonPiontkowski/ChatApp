import java.io.IOException;
import java.net.*;

public class CallListener {
    private static final int PORT = 28411;
    private boolean busy;
    private SocketAddress listenAddress;
    private String localNick;
    private SocketAddress remoteAddress;
    private String remoteNick;
    Connection connection;
    Socket socket;

    public CallListener(){}
    public CallListener(String localNick) throws UnknownHostException{
        this.localNick = localNick;
        this.listenAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(),PORT);
    }
    public CallListener(String localNick,String localIp){
        this.localNick = localNick;
        this.listenAddress = new InetSocketAddress(localIp, PORT);
    }

    public Connection getConnection(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            return new Connection(socket);
        } catch (IOException e){
            // JDialogue with exception
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
    public Socket getSocket(){return this.socket;}
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
    public void setRemoteNick(String remoteNick){this.remoteNick = remoteNick;}

}
