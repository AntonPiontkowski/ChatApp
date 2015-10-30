import java.net.Socket;
import java.net.SocketAddress;

public class Caller {
    private String localNick;
    private SocketAddress remoteAddress;
    private String remoteNick;
    private CallStatus status;

    public Caller(){}
    public Caller(String localNick,SocketAddress remoteAddress){
        this.localNick = localNick;
        this.remoteAddress = remoteAddress;
    }
    public Caller(String localNick,String ip){
        this.localNick = localNick;
    }

    // public Connection call(){}
    public String getLocalNick(){
        return this.localNick;
    }
    public SocketAddress getRemoteAddress(){
        return this.remoteAddress;
    }
    public CallStatus getStatus(){
        return this.status;
    }
    public void setLocalNick(String localNick){
        this.localNick = localNick;
    }
    public void setRemoteAddress(SocketAddress remoteAddress){
        this.remoteAddress = remoteAddress;
    }
    public static void main(String[] args){}

    public enum CallStatus{
        BUSY,NOT_ACCESIBLE,OK,REJECTED;
    }


}
