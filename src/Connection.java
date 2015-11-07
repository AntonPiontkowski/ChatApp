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
        this.printer = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));
        this.scanner = new Scanner(s.getInputStream());
    }

    public void accept() {
        this.printer.print(Command.CommandType.ACCEPT.toString() + "\n");
        this.printer.flush();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void disconnect() throws IOException {
        this.printer.print(Command.CommandType.DISCONNECT.toString() + "\n");
        this.printer.flush();
        close();
    }

    public Command receive() throws IOException {
        String command = this.scanner.nextLine();
        return new Command(Command.getType(command));
    }

    public String receiveMessage() {
        return scanner.nextLine();
    }

    public String[] receiveNickVer() {
        String line = scanner.nextLine();
        String[] checking = line.split(" ");
        /*checking for the right answer
        from another user        */
        if ((checking[0].equals("ChatApp")) & (checking[1].equals("2015")) & (checking[2].equals("user"))) {
            String[] info = new String[2];
            info[0] = checking[0] + " " + checking[1];
            info[1] = checking[4];
            return info;
        } else {
            this.reject();
            return null;
        }
    }

    public void reject() {
        this.printer.print(Command.CommandType.REJECT.toString());
    }

    public void sendMessage(String msg) {
        this.printer.print(Command.CommandType.MESSAGE.toString() + "\n");
        this.printer.print(msg + "\n");
        this.printer.flush();
    }

    public void sendNickBusy(String ver, String nick) {
        this.printer.print(Command.CommandType.NICK.toString());
        this.printer.print(ver + " user " + nick + " busy" + "\n");
        this.printer.flush();
    }

    public void sendNickHello(String ver, String nick) {
        this.printer.print(Command.CommandType.NICK.toString());
        this.printer.print(ver + " user " + nick + "\n");
        this.printer.flush();
    }
}
