import java.io.IOException;
import java.net.*;

public class Caller {
    private String localNick;
    private SocketAddress remoteAddress;
    private static final int PORT = 28411;
    private String remoteNick;
    private CallStatus status;

    public Caller() {
        this.localNick = "unnamed";
        this.remoteAddress = getRemoteAddress();
    }

    public Caller(String localNick) {
    }

    public Caller(String localNick, SocketAddress remoteAddress) {
        this.localNick = localNick;
        this.remoteAddress = remoteAddress;
    }

    public Caller(String localNick, String ip) {
        this.localNick = localNick;
        this.remoteAddress = new InetSocketAddress(ip, PORT);
    }

    public Connection call() {
        try {
            Socket s = new Socket();
            s.connect(this.remoteAddress);
            Connection connection = new Connection(s);
            // ADD USING OF CALLSTATUS
            return connection;
        } catch (IOException e) {
            // EDIT LATER
            e.printStackTrace();
            return null;
        }
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

    public enum CallStatus {
        BUSY, NOT_ACCESIBLE, OK, REJECTED;
    }

}
