import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class Message extends JTextArea{
    String nick;
    String date;
    String msgText;


    private Color blue = new Color(90, 150, 200);
    private Color orng = new Color(215, 100, 50);

    public Message(String nick, String date, String msgText, boolean isUser){
        this.nick = nick;
        this.date = date;
        this.msgText = msgText;
        setLineWrap(true);
        setWrapStyleWord(true);
        setAutoscrolls(true);
        setFont(this.getFont().deriveFont(14f));
        if (isUser)
            setForeground(orng);
        else
            setForeground(blue);
        StringBuilder text = new StringBuilder(nick + "\n" + date.toString() + "\n\n" + msgText);
        this.setText(text.toString());
    }
}
