import java.io.IOException;

import java.io.InputStream;
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

    public synchronized void accept() {
        this.printer.print("Accepted\n");
    }

    public void close() throws IOException {
        socket.close();
    }

    public synchronized void disconnect() throws IOException {
        this.printer.println("Disconnected\n");
        close();
    }

     public synchronized Command receive() throws IOException{
         String command = this.scanner.nextLine();
         return null;
         // FIRSTLY EDIT COMMAND NESTED CLASS
     }
    public synchronized void reject() {
        this.printer.print("Rejected\n");
    }

    public synchronized void sendMessage(String msg) {
        this.printer.print("Message\n");
        this.printer.println(msg);
    }

    public synchronized void sendNickBusy(String ver, String nick) {
        this.printer.print(nick + "\n");
        this.printer.print("Busy\n");
        this.printer.print(ver + ". User <" + nick + "> is busy!\n");
    }

    public synchronized void sendNickHello(String ver, String nick) {
        this.printer.print(nick + "\n");
        this.printer.print(ver + ". User <" + nick + "> says hello!\n");
    }
}
