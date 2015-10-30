import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketAddress;

public class MainFrame extends JFrame {
    private String localNick;

    public MainFrame()  {
        setSize(600, 400);
        getContentPane().setBackground(Color.WHITE);
        setLocation(220,100);
        setTitle("ChatApp 2015");
        setUndecorated(false);
        setResizable(false);
        setIconImage(new ImageIcon("icon.png").getImage());
    }
}
