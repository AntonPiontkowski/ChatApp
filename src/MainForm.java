import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainForm extends JFrame {
    Caller caller;
    Connection connection;
    private int xMouse;
    private int yMouse;

    final JLabel close;
    final JLabel tray;
    final JLabel bar;
    final JTextArea localNickText;
    final JLabel disconnect;
    final JTextArea remoteAddressText;
    final JLabel connect;
    final JLabel apply;
    final JTextArea newMsg;
    final JLabel send;

    DefaultListModel listModel;
    JTextArea massageDisplay;

    public MainForm() {
        setBounds(200, 100, 700, 400);
        setUndecorated(true);
        setLayout(null);
        setIconImage(new ImageIcon("icon.png").getImage());
        close = new JLabel("");
        close.setIcon(new ImageIcon("gui/close.png"));
        close.setBounds(673, 7, 15, 15);
        close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                close.setIcon(new ImageIcon("gui/closePressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                close.setIcon(new ImageIcon("gui/close.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setIcon(new ImageIcon("gui/closeEntered.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setIcon(new ImageIcon("gui/close.png"));
            }
        });
        add(close);

        tray = new JLabel("");
        tray.setIcon(new ImageIcon("gui/tray.png"));
        tray.setBounds(653, 7, 15, 15);
        tray.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setState(ICONIFIED);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                tray.setIcon(new ImageIcon("gui/trayPressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                tray.setIcon(new ImageIcon("gui/tray.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                tray.setIcon(new ImageIcon("gui/trayEntered.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tray.setIcon(new ImageIcon("gui/tray.png"));
            }
        });
        add(tray);

        bar = new JLabel("");
        bar.setBounds(0, 0, 700, 31);
        bar.setIcon(new ImageIcon("gui/bar.png"));
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
                bar.setIcon(new ImageIcon("gui/bar.png"));
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                bar.setIcon(new ImageIcon("gui/barOff.png"));
            }
        });
        bar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                xMouse = e.getX();
                yMouse = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        bar.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - xMouse, y - yMouse);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        add(bar);

        localNickText = new JTextArea();
        localNickText.setBounds(480, 86, 130, 20);
        add(localNickText);

        JTextArea remoteNickText = new JTextArea();
        remoteNickText.setEditable(false);
        remoteNickText.setEnabled(false);
        remoteNickText.setBounds(480, 305, 197, 20);
        add(remoteNickText);

        disconnect = new JLabel("");
        disconnect.setEnabled(false);
        disconnect.setBounds(589, 252, 93, 33);
        disconnect.setIcon(new ImageIcon("gui/disconnectBtn.png"));
        disconnect.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ��� ���������� ���� ������ disconnect
                try{
                    //connection.disconnect();
                    connection.close();
                }catch (IOException ex){}

                disconnect.setEnabled(false);
                connect.setEnabled(true);
                //apply.setEnabled(true);
                //localNickText.setEnabled(true);
                remoteAddressText.setEnabled(true);
                send.setEnabled(false);
                newMsg.setEnabled(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                disconnect.setIcon(new ImageIcon("gui/disconnectBtnPressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                disconnect.setIcon(new ImageIcon("gui/disconnectBtn.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                disconnect.setIcon(new ImageIcon("gui/disconnectBtnEntered.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                disconnect.setIcon(new ImageIcon("gui/disconnectBtn.png"));
            }
        });
        add(disconnect);

        remoteAddressText = new JTextArea();
        remoteAddressText.setEnabled(false);
        remoteAddressText.setBounds(480, 215, 197, 20);
        add(remoteAddressText);

        connect = new JLabel("");
        connect.setEnabled(false);
        connect.setBounds(476, 252, 93, 33);
        connect.setIcon(new ImageIcon("gui/connectBtn.png"));
        connect.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ��� ���������� ���� ������ connect
                if (remoteAddressText.getText().equals("")){
                    return;
                    //connection error
                }
                else {
                    connect.setEnabled(false);
                    remoteAddressText.setEnabled(false);
                    /**
                     * ��� �����
                     */
                    newMsg.setEnabled(true);
                    send.setEnabled(true);
                    disconnect.setEnabled(true);
                    remoteAddressText.setEnabled(true);
                    try {
                        Socket s = new Socket(remoteAddressText.getText(), 28411);
                        connection = new Connection(s);
                        connection.sendNickHello("2015", localNickText.getText());
                    } catch (Exception ex) {
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                connect.setIcon(new ImageIcon("gui/connectBtnPressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                connect.setIcon(new ImageIcon("gui/connectBtn.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                connect.setIcon(new ImageIcon("gui/connectBtnEntered.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                connect.setIcon(new ImageIcon("gui/connectBtn.png"));
            }
        });
        add(connect);

        apply = new JLabel("");
        apply.setBounds(624, 83, 57, 24);
        apply.setIcon(new ImageIcon("gui/applyBtn.png"));
        apply.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ��� ���������� ���� ������ apply
                if (localNickText.getText().equals("")) {
                    localNickText.setText("unnamed");
                }
                apply.setEnabled(false);
                localNickText.setEnabled(false);
                connect.setEnabled(true);
                remoteAddressText.setEnabled(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                apply.setIcon(new ImageIcon("gui/applyBtnPressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                apply.setIcon(new ImageIcon("gui/applyBtn.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                apply.setIcon(new ImageIcon("gui/applyBtnEntered.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                apply.setIcon(new ImageIcon("gui/applyBtn.png"));
            }
        });
        add(apply);

        JLabel fieldsBG = new JLabel("");
        fieldsBG.setBounds(0, 0, 700, 400);
        fieldsBG.setIcon(new ImageIcon("gui/fieldsBG.png"));
        add(fieldsBG);

        newMsg = new JTextArea("");
        newMsg.setEnabled(false);
        newMsg.setBackground(Color.GRAY);
        newMsg.setForeground(Color.ORANGE);
        newMsg.setBounds(70, 356, 367, 20);
        add(newMsg);

        send = new JLabel("");
        send.setEnabled(false);
        send.setBounds(10, 335, 58, 58);
        send.setIcon(new ImageIcon("gui/sendBtn.png"));
        send.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ��� ���������� ���� ������ ������ send
                connection.sendMessage(newMsg.getText());
                listModel.addElement(localNickText.getText() + ":" + newMsg.getText() + "\n");
                newMsg.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                send.setIcon(new ImageIcon("gui/sendBtnPressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                send.setIcon(new ImageIcon("gui/sendBtn.png"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                send.setIcon(new ImageIcon("gui/sendBtnEntered.png"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                send.setIcon(new ImageIcon("gui/sendBtn.png"));
            }
        });
        add(send);

        //JLabel messagingBG = new JLabel("");
        //messagingBG.setBounds(25,55,416,322);
        //messagingBG.setIcon(new ImageIcon("gui/messagingBG.png"));
        //add(messagingBG);

        listModel = new DefaultListModel();
        JList massageDisplay = new JList(listModel);
        JScrollPane pane = new JScrollPane(massageDisplay);
        pane.setBounds(25, 55, 412, 301);
        pane.setFocusable(false);
        massageDisplay.setEnabled(false);
        add(pane);

        JLabel background = new JLabel("");
        background.setBounds(0, 0, 700, 400);
        background.setIcon(new ImageIcon("gui/background.png"));
        add(background);
    }
    public static void main(String[] args){
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e ){}
        MainForm mainForm = new MainForm();
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.setVisible(true);
    }

}
