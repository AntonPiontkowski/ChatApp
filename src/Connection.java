import java.io.IOException;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;

    public Connection(Socket s) throws IOException {
        this.socket = s;
        this.printer = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"), true);
        this.scanner = new Scanner(s.getInputStream());
    }

    public void accept() {
        this.printer.print("Accepted\n");
    }

    public void close() throws IOException {
        socket.close();
    }

    public void disconnect() throws IOException {
        this.printer.println("Disconnected\n");
        close();
    }

    /*
     * public Command receive(){
     *
     * }
     */
    public void reject() {

    }

    public void sendMessage(String msg) {
        this.printer.print("Message\n");
        this.printer.println(msg);
    }

    public void sendNickBusy(String ver, String nick) {
        this.printer.print("Busy\n");
        this.printer.print(ver + ". User <" + nick + "> is busy!\n");
    }

    public void sendNickHello(String ver, String nick) {
        this.printer.print("Nick \n");
        this.printer.print(ver + ". User <" + nick + "> says hello!\n");
    }
}
