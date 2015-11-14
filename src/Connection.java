import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;
    private String command;

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

    public Command receive() {
        try {
            command = this.scanner.nextLine();
            if (command.substring(0, 7).equals("ChatApp"))
                return new NickCommand();
            else if (command.equals("Message")) {
                return new MessageCommand();
            } else {
                if (Command.getType(command) != null)
                    return new Command(Command.getType(command));
                else return null;
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public String receiveMessage() {
        return scanner.nextLine();
    }

    public String getCommandText() {
        return this.command;
    }

    public String[] receiveNickVer(String line) {
        String[] checking = line.split(" ");
//        Checking if the pal's HelloMessage is right
        if ((checking[0].equals("ChatApp")) & (checking[1].equals("2015")) & (checking[2].equals("user"))) {
            if (checking.length == 4) {
                String[] info = new String[2];
                info[0] = checking[0] + " " + checking[1];
                info[1] = checking[3];
                return info;
            } else {
                String[] info = new String[3];
                info[0] = checking[0] + " " + checking[1];
                info[1] = checking[3];
                info[2] = checking[4];
                return info;
            }
        } else {
            this.reject();
            return null;
        }
    }

    public void reject() {
        this.printer.print(Command.CommandType.REJECT.toString() + "\n");
    }

    public void sendMessage(String msg) {
        this.printer.print("Message" + "\n");
        this.printer.print(msg + "\n");
        this.printer.flush();
    }

    public void sendNickBusy(String ver, String nick) {
        this.printer.print(ver + " user " + nick + " busy" + "\n");
        this.printer.flush();
    }

    public void sendNickHello(String ver, String nick) {
        this.printer.print(ver + " user " + nick + "\n");
        this.printer.flush();
    }
}
