import java.io.IOException;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;
    private Command command;

    public Connection(Socket s) throws IOException{
        this.socket  = s;
        this.printer = new PrintWriter(s.getOutputStream(),true);
        this.scanner = new Scanner(s.getInputStream(),"UTF-8");
    }

    public void accept(){
        this.command.type = Command.CommandType.ACCEPT;
        this.printer.println(command.type.name());
    }
    public void close() throws IOException{
        socket.close();
    }
    public void disconnect() throws IOException{
        this.command.type = Command.CommandType.DISCONNECT;
        this.printer.println(this.command.type.name());
        close();
    }
    //public boolean isOpen(){}
    public Command receive(){
        return new Command(Command.CommandType.valueOf(this.scanner.nextLine()));
    }
    public void reject(){
        this.command.type = Command.CommandType.REJECT;
    }
    public void sendMessage(String msg){
        //this.command.type = Command.CommandType.MESSAGE;
        //this.printer.println(this.command.type.name());
        this.printer.println(msg);
    }
    public void sendNickBusy(String ver,String nick){
        this.printer.print(ver + ". User <" + nick
                + "> is busy!" + 0x0a);
    }
    public void sendNickHello(String ver,String nick){
        //this.command.type = Command.CommandType.NICK;
        //this.printer.println(this.command.type.name());
        this.printer.println(ver + ". User <" + nick
                + "> says hello!" + 0x0a);
    }
}
