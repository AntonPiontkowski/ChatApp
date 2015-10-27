import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;

    public Connection(Socket s){
        this.socket  = s;
    }

    public void accept(){}
    public void close(){}
    public void disconnect(){}
    public void isOpen(){}
    // public Command receive(){}
    public void reject(){}
    public void sendMessage(String msg){}
    public void sendNickBusy(String ver,String nick){}
    public void sendNickHello(String ver,String nick){}
    public static void main(String[] args){}

    public static class Command{
        public CommandType type;
        public Command(CommandType t){
            this.type = t;
        }
    }
    public static enum CommandType{
        ACCEPT,DISCONNECT,MESSAGE,NICK,REJECT;

        public static CommandType valueOf(String name){

        }
        public static CommandType[] values(){

        }
    }
}
