import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    /**
     * Объекты Connection & Caller для
     * возможности воздействия на них
     * с помощью кнопок на фрейме
     */
    Connection connection;
    Caller caller;
    public MainFrame()  {
        setSize(600,400);
        getContentPane().setBackground(Color.WHITE);
        setLocation(220,100);
        setTitle("ChatApp 2015");
        setResizable(false);
        setIconImage(new ImageIcon("icon.png").getImage());

    }
    private class ButtonAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
