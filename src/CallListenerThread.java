import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketAddress;
import java.util.Observable;
import java.util.Scanner;

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
    public void run() {
        while(true){
            Connection connection = callListener.getConnection();
            if (Thread.currentThread().isInterrupted()){
                break;
            }else if (connection != null){
                try{
                    PrintWriter send = new PrintWriter(new OutputStreamWriter(callListener.getSocket().getOutputStream(), "UTF-8"), true);
                    Scanner recieve = new Scanner(callListener.getSocket().getInputStream());
                    String income = "";
                    income += recieve.next("ChatApp");
                    income += recieve.next("2015");
                    income += recieve.next("user");
                    send.print("ChatApp 2015 user " + callListener.getLocalNick() + "\n");
                    if (income != "ChatApp 2015 user "){
                        break;
                    }else{
                        callListener.setRemoteNick(recieve.next());
                        // ask user to accept or reject
                        //if reject: connection.reject()
                        //if accept: create new dialog
                    }
                }catch (IOException ex){
                    //TODO: handle exception
                }

            }
        }
    }
    public static void main(String[] args){}
}
