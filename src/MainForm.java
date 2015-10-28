import javax.swing.*;
import java.awt.*;

public class MainForm {
    public static void main(String[] args){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        MainFrame mainFrame = new MainFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

}
