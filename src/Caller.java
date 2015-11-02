import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Caller {
    private String localNick;
    private SocketAddress remoteAddress;
    private final int port = 28411;
    private String ip;
    private String remoteNick;
    private CallStatus status;

    public Caller() {
        this.localNick = "unnamed";
        this.remoteAddress = getRemoteAddress();
    }

    public Caller(String localNick, SocketAddress remoteAddress) {
        this.localNick = localNick;
        this.remoteAddress = remoteAddress;
    }

    public Caller(String localNick, String ip) {
        this.localNick = localNick;
        this.ip = ip;
    }

    public Connection call() throws Exception {
        Connection connection = new Connection(new Socket(InetAddress.getByName(ip), port));
        return connection;

    }

    public String getLocalNick() {
        return this.localNick;
    }

    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public CallStatus getStatus() {
        return this.status;
    }

    public void setLocalNick(String localNick) {
        this.localNick = localNick;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public static void main(String[] args) {
    }

    public enum CallStatus {
        BUSY, NOT_ACCESIBLE, OK, REJECTED;
    }

}
