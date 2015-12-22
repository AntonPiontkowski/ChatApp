import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/*

CLASS THAT IS USED FOR ALL DATA SENDING AND RECEIVING OPERATIONS

 */

public class Connection {
    private PrintWriter printer;
    private Scanner scanner;
    private Socket socket;
    private String fileName;

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

    public void setFiles() {
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Open File");
        if (ret == JFileChooser.APPROVE_OPTION) {
            this.fileName = fileopen.getSelectedFile().toString();
        }
    }

    public void sendFiles() {
        System.out.println("Sending");
        this.printer.print("File" + "\n");
        this.printer.flush();
        DataOutputStream outD;
        try {
            outD = new DataOutputStream(socket.getOutputStream());

            File f = new File(fileName);

            outD.writeLong(f.length());// отсылаем размер файла
            outD.writeUTF(f.getName());// отсылаем имя файла

            FileInputStream in = new FileInputStream(f);
            byte[] buffer = new byte[64 * 1024];
            int count;

            while ((count = in.read(buffer)) != -1) {
                outD.write(buffer, 0, count);
            }
            outD.flush();
            in.close();
           // socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sended");
    }

    public String receiveFile() {
        System.out.println("Try");
        try {
            ServerSocket ss = new ServerSocket(Constants.FILE_PORT);

            while (true) {
                Socket s = ss.accept();

                java.io.InputStream in = s.getInputStream();
                DataInputStream din = new DataInputStream(in);

                long fileSize = din.readLong(); // получаем размер файла

                String fileName = din.readUTF(); // прием имени файла
//                System.out.println("Имя файла: " + fileName + "\n");
//                System.out.println("Размер файла: " + fileSize + " байт\n");

                byte[] buffer = new byte[64 * 1024];
                FileOutputStream outF = new FileOutputStream(fileName);
                int count, total = 0;

                while ((count = din.read(buffer)) != -1) {
                    total += count;
                    outF.write(buffer, 0, count);

                    if (total == fileSize) {
                        break;
                    }
                }
                outF.flush();
                outF.close();
//                System.out.println("Файл принят\n---------------------------------\n");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

}
