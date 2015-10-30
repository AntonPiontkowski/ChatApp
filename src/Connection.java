import java.io.IOException;
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
    public void disconnect() throws IOException{
        printer.println("User aborted the connection");
        socket.close();
    }
    public void isOpen(){}
    // public Command receive(){}
    public void reject(){}
    public void sendMessage(String msg){
        printer.println(msg);
    }
    public void sendNickBusy(String ver,String nick){
        printer.print("ChatApp 2015. User <" + nick
                + "> is busy!" + 0x0a);
    }
    public void sendNickHello(String ver,String nick){
        printer.print("ChatApp 2015. User <" + nick
                + "> says hello!" + 0x0a);
    }
    public static void main(String[] args){}

    public static class Command{
        public CommandType type;
        public Command(CommandType t){
            this.type = t;
        }
        public Command() {
        }
    }
    public enum CommandType{
        ACCEPT,DISCONNECT,MESSAGE,NICK,REJECT;
    }
    public static class MessageCommand extends  Command{
        public String message;
        public CommandType type;

        public MessageCommand(String message){
            this.message = message;
        }
    }
    public static class NickCommand extends Command{
        public boolean busy;
        public String nick;
        public String version;

        public NickCommand(boolean busy,String nick,String version){

        }
    }
}
