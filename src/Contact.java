import javax.swing.*;
import java.awt.*;

public class Contact extends JPanel {
    private String nick;
    private String addr;
    private boolean isOnline;

    public Contact(String nick, String addr, boolean isOnline) {
        this.nick = nick;
        this.addr = addr;
        this.isOnline = isOnline;
        JLabel nickLabel = new JLabel(nick);
        nickLabel.setForeground(Colors.gray2);
        nickLabel.setFont(nickLabel.getFont().deriveFont(16f));
        this.add(nickLabel);
        JLabel status = new JLabel();
        status.setMinimumSize(new Dimension(15, 15));
        status.setMaximumSize(new Dimension(15, 15));
        if (isOnline)
            status.setIcon(new ImageIcon(getClass().getResource("gui/frame/userOn.png")));
        else
            status.setIcon(new ImageIcon(getClass().getResource("gui/frame/userOff.png")));
        this.add(status);
        this.setBackground(Colors.dark2);
        this.setMaximumSize(new Dimension(200, 35));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Colors.dark3));
    }

    public String getNick() {
        return this.nick;
    }

    public String getAddr() {
        return this.addr;
    }

    public void setDefaulBackground() {
        this.setBackground(Colors.dark1);
    }
}
