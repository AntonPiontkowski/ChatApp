import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*

CLASS THAT IS USED FOR ALL DATA SENDING AND RECEIVING OPERATIONS

 */

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            this.printer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            this.scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        this.socket.close();
    }

    // Sending commands
    public void send(String line) {
        this.printer.print(line + "\n");
        this.printer.flush();
    }

    // Sending messages
    public void sendMsg(String line) {
        this.printer.print("Message" + "\n");
        this.printer.flush();
        this.printer.print(line + "\n");
        this.printer.flush();
    }

    // Sending protocol greetings
    public void sendNick(String ver, String nick, boolean busy) {
        StringBuilder line = new StringBuilder();
        line.append(ver + " user " + nick);
        if (busy)
            line.append(" busy");
        this.printer.print(line + "\n");
        this.printer.flush();
    }

    // Receiving command,messages
    public String receive() {
        try {
            return this.scanner.nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public SocketAddress getSocketAddress() {
        return this.socket.getRemoteSocketAddress();
    }
}
