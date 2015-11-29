import javax.swing.*;
import java.awt.*;

public class Contact extends JPanel{
    String addr;
    boolean isOnline;

    public Contact (String nick, String addr, boolean isOnline){
        this.addr = addr;
        this.isOnline = isOnline;
        JLabel nickLabel = new JLabel(nick);
        nickLabel.setForeground(Colors.gray1);
        nickLabel.setFont(nickLabel.getFont().deriveFont(12f));
        this.add(nickLabel);
        JLabel status = new JLabel();
        status.setMinimumSize(new Dimension(15, 15));
        status.setMaximumSize(new Dimension(15, 15));
        if (isOnline)
            status.setIcon(new ImageIcon(getClass().getResource("gui/form/userOn.png")));
        else
            status.setIcon(new ImageIcon(getClass().getResource("gui/form/userOff.png")));
        this.add(status);
    }
}
