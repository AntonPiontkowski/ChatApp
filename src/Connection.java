import java.io.IOException;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;

    public Connection(Socket s) throws IOException{
        this.socket  = s;
        this.printer = new PrintWriter(s.getOutputStream(),true);
        this.scanner = new Scanner(s.getInputStream());
    }

    public void accept(){

    }
    public void close() throws IOException{
        socket.close();
    }
    public void disconnect() throws IOException{
        this.printer.println(Command.CommandType.DISCONNECT);
        close();
    }
    //public boolean isOpen(){}
    public Command receive(){
        return new Command(Command.CommandType.valueOf(this.scanner.nextLine()));
    }
    public void reject(){

    }
    public void sendMessage(String msg){
        this.printer.println(Command.CommandType.MESSAGE.name());
        this.printer.println(msg);
    }
    public void sendNickBusy(String ver,String nick){
        //this.command.type = Command.CommandType.NICK;
        //this.printer.print(this.command.type.name() + "\n");
        this.printer.print(ver + ". User <" + nick
                + "> is busy!\n");
    }
    public void sendNickHello(String ver,String nick){
        //this.command.type = Command.CommandType.NICK;
        //this.printer.print(this.command.type.name() + "\n");
        this.printer.print(ver + ". User <" + nick
                + "> says hello!");
    }
}
