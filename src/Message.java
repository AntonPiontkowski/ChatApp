import javax.swing.*;
import java.awt.*;

public class Message extends JPanel {

    public Message(String nick, String date, String msgText, boolean isUser) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setMaximumSize(new Dimension(1000, 80));
        this.setBorder(BorderFactory.createLineBorder(Colors.dark1));
        this.add(new MessageInfo(nick, date, isUser));
        this.add(new MessageText(msgText));
    }


    private class MessageInfo extends JTextArea {

        public MessageInfo(String nick, String date, boolean isUser) {
            if (isUser)
                setForeground(Colors.blue);
            else
                setForeground(Colors.orng);
            StringBuilder line = new StringBuilder(nick + "  " + date);
            this.setOpaque(false);
            this.setMaximumSize(new Dimension(1000, 20));
            this.append(line.toString());
        }

    }

    private class MessageText extends JTextArea {

        public MessageText(String msg) {
            this.setFont(this.getFont().deriveFont(18f));
            this.setForeground(Color.LIGHT_GRAY);
            this.setOpaque(false);
            this.setMaximumSize(new Dimension(1000, 40));
            this.append(msg);
        }

    }

}
