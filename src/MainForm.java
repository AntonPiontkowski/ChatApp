import jdk.nashorn.internal.codegen.CompilerConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;


public class MainForm extends JFrame {
    private Caller caller;
    private Connection connection;
    private CallListener callListener;
    private CommandListenerThread commandThread;
    private CallListenerThread callThread;
    private int xMouse;
    private int yMouse;

    private static final int MAX_MSG_LENGTH = 57;
    private static final int MAX_NICK_LENGTH = 15;
    private static final int MIN_NICK_LENGTH = 3;
    private static final int PORT = 28411;

    final JLabel close;
    final JLabel tray;
    final JLabel bar;
    final JTextArea localNickText;
    final JTextArea remoteNickText;
    final JLabel disconnect;
    final JTextArea remoteAddressText;
    final JLabel connect;
    final JLabel apply;
    final JTextArea newMsg;
    final JTextArea messagingArea;

    public MainForm() {
        setBounds(200, 100, 700, 400);
        setUndecorated(true);
        setLayout(null);
        setIconImage(new ImageIcon("icon.png").getImage());
        close = new JLabel("");
        close.setIcon(new ImageIcon("gui/close.png"));
        close.setBounds(673, 7, 15, 15);
        close.setToolTipText("Close");
        close.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Sound.EXIT.play();
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
        tray.setToolTipText("Minimize");
        tray.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setState(ICONIFIED);
                Sound.TRAY.play();
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

        messagingArea = new JTextArea("");
        messagingArea.setLineWrap(true);
        messagingArea.setEditable(false);
        final JScrollPane messagePane = new JScrollPane(messagingArea);
        messagePane.setVisible(true);
        messagePane.setBounds(29, 60, 395, 275);
        add(messagePane);

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
        localNickText.setColumns(15);
        localNickText.setRows(1);
        localNickText.setToolTipText("Your nickname");
        localNickText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                StringBuilder b = new StringBuilder(localNickText.getText());
                if (localNickText.getText().length() > MAX_NICK_LENGTH) {
                    localNickText.setText(b.delete(MAX_NICK_LENGTH, b.length()).toString());
                }
                if (e.getKeyChar() == '\n') {
                    localNickText.setText(b.toString().trim());
                }
            }
        });

        add(localNickText);
        remoteNickText = new JTextArea();
        remoteNickText.setEditable(false);
        remoteNickText.setEnabled(false);
        remoteNickText.setToolTipText("Mate's nickname(auto fill)");
        remoteNickText.setBounds(480, 305, 197, 20);
        add(remoteNickText);

        disconnect = new JLabel("");
        disconnect.setEnabled(false);
        disconnect.setBounds(589, 252, 93, 33);
        disconnect.setIcon(new ImageIcon("gui/disconnectBtn.png"));
        disconnect.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (disconnect.isEnabled()) Sound.CLICK.play();
                try {
                    connection.disconnect();
                } catch (IOException ex) {
                }

                disconnect.setEnabled(false);
                connect.setEnabled(true);
                remoteAddressText.setEnabled(true);
                newMsg.setEditable(false);
                messagingArea.setText("");
                if (!remoteAddressText.getText().equals(""))
                    remoteAddressText.setText("");
                if (!remoteNickText.getText().equals(""))
                    remoteNickText.setText("");
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
        remoteAddressText.setToolTipText("Address to connect to");
        remoteAddressText.setRows(1);
        remoteAddressText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    String b = new String();
                    b = remoteAddressText.getText();
                    remoteAddressText.setText(b.trim());
                }
            }
        });
        add(remoteAddressText);

        connect = new JLabel("");
        connect.setEnabled(false);
        connect.setBounds(476, 252, 93, 33);
        connect.setIcon(new ImageIcon("gui/connectBtn.png"));
        connect.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (connect.isEnabled()) Sound.CLICK.play();
                try {
                    if (caller == null)
                        caller = new Caller(localNickText.getText(), remoteAddressText.getText());
                    else
                        caller.setRemoteAddress(new InetSocketAddress(remoteAddressText.getText(), PORT));
                    connection = caller.call();
                    if (connection != null) {
                        if (connect.isEnabled()) {
                            connect.setEnabled(false);
                            remoteAddressText.setEnabled(false);
                            newMsg.setEditable(true);
                            disconnect.setEnabled(true);
                            remoteAddressText.setEnabled(true);
                        }
                    }
                } catch (Exception ex) {  // At this rate - all exceptions
                    ex.printStackTrace();
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
                if (apply.isEnabled()) Sound.CLICK.play();
//                try {
                    if (localNickText.getText().equals("") | localNickText.getText().length() < MIN_NICK_LENGTH) {
                        localNickText.setText("unnamed");
                    }
                    if (localNickText.getText().length() > MIN_NICK_LENGTH && localNickText.getText().length() < MAX_NICK_LENGTH) {
                        if (!connect.isEnabled() && apply.isEnabled()) {
                            connect.setEnabled(true);
                        }
                        apply.setEnabled(false);
                        localNickText.setEnabled(false);
                        remoteAddressText.setEnabled(true);
                    }
                    /*callListener = new CallListener(localNickText.getText(), InetAddress.getLocalHost().getHostAddress());
                    callThread = new CallListenerThread(callListener);
                    callThread.addObserver(new Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            // new JDialogue with a request to begin chat
                        }
                    });
                     SwingUtilities.invokeLater(callListener)*/
//                } catch (UnknownHostException e2) {
//                    e2.printStackTrace();
//                }
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
        newMsg.setBackground(Color.GRAY);
        newMsg.setForeground(Color.ORANGE);
        newMsg.setBounds(32, 340, 400, 20);
        newMsg.setEditable(false);
        newMsg.setToolTipText("Enter your message here");
        newMsg.setColumns(50);
        newMsg.setRows(1);
        newMsg.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                StringBuilder b = new StringBuilder(newMsg.getText());
                if (newMsg.getText().length() > MAX_MSG_LENGTH) {
                    newMsg.setText(b.delete(MAX_MSG_LENGTH, b.length()).toString());
                }
                if (e.getKeyChar() == '\n') {
                    newMsg.setText(b.toString().trim());
                    Sound.OUTGOING.play();
                    connection.sendMessage(newMsg.getText());
                    messagingArea.append(localNickText.getText() + ":" + newMsg.getText() + "\n");
                    newMsg.setText("");
                }
            }
        });
        add(newMsg);

        JLabel messagingBG = new JLabel("");
        messagingBG.setBounds(25, 55, 416, 322);
        messagingBG.setIcon(new ImageIcon("gui/messagingBG.png"));
        add(messagingBG);

        JLabel background = new JLabel("");
        background.setBounds(0, 0, 700, 400);
        background.setIcon(new ImageIcon("gui/background.png"));
        add(background);

        connect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        disconnect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        apply.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.setVisible(true);
    }
}
