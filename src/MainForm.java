
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MainForm extends JFrame {
    private Caller caller;
    private volatile Connection connection;
    private volatile CallListener callListener;
    private CommandListenerThread commandThread;
    private CallListenerThread callThread;
    private int xMouse;
    private int yMouse;

    private static final int MAX_MSG_LENGTH = 57;
    private static final int MAX_NICK_LENGTH = 15;
    private static final int MIN_NICK_LENGTH = 3;
    private static final int PORT = 28411;
    private static final String VER = "ChatApp 2015";

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

//    SwingWorker worker;

    public MainForm() {
        setBounds(200, 100, 700, 400);
        setUndecorated(true);
        setLayout(null);
        setTitle("ChatApp 2015");
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
                    if (connection != null)
                        connection.disconnect();
                } catch (IOException ex) {
                }
                try {
                    disconnecting();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
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
                if (!remoteAddressText.getText().equals("")) {
                    if (connect.isEnabled()) Sound.CLICK.play();
                    if (caller == null)
                        caller = new Caller(localNickText.getText(), remoteAddressText.getText());
                    else
                        caller.setRemoteAddress(new InetSocketAddress(remoteAddressText.getText(), PORT));
                    // TODO EDIT
//                    worker = new SwingWorker<Connection, Void>() {
//                        @Override
//                        public Connection doInBackground() throws Exception {
//                            connecting();
//                            Connection innerConnection = caller.call();
//                            return innerConnection;
//                        }
//                        @Override
//                        public void done(){
//                            try {
//                                connection = get(Long.valueOf(10), TimeUnit.SECONDS);
//                                if (connection == null) {
//                                    disconnecting();
//                                }
//                            }catch(ExecutionException ex){
//                                connection = null;
//                                //TODO Handle Exception
//                            }catch (InterruptedException ex){
//                                connection = null;
//                                //TODO Handle Exception
//                            }catch (TimeoutException ex){
//                                connection = null;
//                                //TODO Handle Exception
//                            }
//                        }
//                    };
//                    worker.execute();
                    connection = caller.call();
                    if (connection != null) {
                        if (connect.isEnabled()) {
                            // TODO CREATE A CommandListenerThread; ADD AN OBSERVER
                            connecting();
                            listenCommands();
                            commandThread.start();
                        }
                    }
                } else {
                    class Attention extends JFrame {

                        private final JLabel ok;
                        private final JLabel text;

                        public Attention() {
                            Toolkit toolkit = Toolkit.getDefaultToolkit();
                            Dimension size = toolkit.getScreenSize();

                            this.setBounds((size.width / 2) - (292 / 2), (size.height / 2) - (134 / 2), 250, 134);
                            this.setUndecorated(true);
                            this.setResizable(false);
                            this.setLayout(null);


                            text = new JLabel("Line input ip -address is empty");
                            text.setBounds(45, 50, 220, 15);
                            text.setForeground(Color.WHITE);
                            this.add(text);

                            ok = new JLabel("");
                            ok.setBounds(160, 101, 81, 24);
                            ok.setIcon(new ImageIcon("gui/AttentionWindow/okBtn.png"));
                            ok.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    setVisible(false);
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    ok.setIcon(new ImageIcon("gui/AttentionWindow/okBtnEntered.png"));
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    ok.setIcon(new ImageIcon("gui/AttentionWindow/okBtn.png"));
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    ok.setIcon(new ImageIcon("gui/AttentionWindow/okBtnEntered.png"));
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                    ok.setIcon(new ImageIcon("gui/AttentionWindow/okBtn.png"));
                                }
                            });
                            this.add(ok);

                            JLabel windowBackground = new JLabel("");
                            windowBackground.setBounds(-3, 0, 310, 140);
                            windowBackground.setIcon(new ImageIcon("gui/AttentionWindow/background.png"));
                            this.add(windowBackground);
                            this.setVisible(true);
                        }

                        public Attention showAttention() {
                            return this;
                        }
                    }
                    Attention attention = new Attention();
                    attention.showAttention();
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

        class AskWindow extends JDialog {

            final JLabel yesButton;
            final JLabel noButton;
            final JLabel topText;
            final JLabel buttonText;

            public AskWindow() {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension size = toolkit.getScreenSize();
                int screenWidth = size.width;
                int screenHeight = size.height;

                this.setBounds((screenWidth / 2) - (292 / 2), (screenHeight / 2) - (134 / 2), 292, 134);
                this.setUndecorated(true);
                this.setResizable(false);
                this.setLayout(null);

                yesButton = new JLabel("");
                yesButton.setBounds(35, 101, 80, 33);
                yesButton.setIcon(new ImageIcon("gui/JDialog/dialogueYesBtn.png"));
                yesButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        connection.accept();
                        String[] remoteInfo = connection.receiveNickVer(connection.getCommandText());
                        remoteNickText.setText(remoteInfo[1]);
                        remoteAddressText.setText(callListener.getRemoteAddress().toString());
                        connecting();
                        listenCommands();
                        commandThread.start();
                        getWindow().setVisible(false);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        yesButton.setIcon(new ImageIcon("gui/JDialog/dialogueYesBtnPressed.png"));
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        yesButton.setIcon(new ImageIcon("gui/JDialog/dialogueYesBtn.png"));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        yesButton.setIcon(new ImageIcon("gui/JDialog/dialogueYesBtnEntered.png"));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        yesButton.setIcon(new ImageIcon("gui/JDialog/dialogueYesBtn.png"));
                    }
                });
                this.add(yesButton);

                noButton = new JLabel("");
                noButton.setBounds(177, 101, 80, 33);
                noButton.setIcon(new ImageIcon("gui/JDialog/dialogueNoBtn.png"));
                noButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        connection.reject();
                        try {
                            connection.close();
                            disconnecting();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        getWindow().setVisible(false);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        noButton.setIcon(new ImageIcon("gui/JDialog/dialogueNoBtnPressed.png"));
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        noButton.setIcon(new ImageIcon("gui/JDialog/dialogueNoBtn.png"));
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        noButton.setIcon(new ImageIcon("gui/JDialog/dialogueNoBtnEntered.png"));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        noButton.setIcon(new ImageIcon("gui/JDialog/dialogueNoBtn.png"));
                    }
                });
                this.add(noButton);

                topText = new JLabel("Another user wants to speak to you.");
                topText.setBounds(45, 40, 220, 15);
                topText.setForeground(Color.WHITE);
                this.add(topText);

                buttonText = new JLabel("Accept him?");
                buttonText.setBounds(108, 60, 80, 15);
                buttonText.setForeground(Color.WHITE);
                this.add(buttonText);

                JLabel windowBackground = new JLabel("");
                windowBackground.setBounds(-3, 0, 310, 140);
                windowBackground.setIcon(new ImageIcon("gui/JDialog/dialogueForm.png"));
                this.add(windowBackground);

                this.setVisible(true);
            }

            public AskWindow getWindow() {
                return this;
            }
        }

        apply = new JLabel("");
        apply.setBounds(624, 83, 57, 24);
        apply.setIcon(new ImageIcon("gui/applyBtn.png"));
        apply.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (apply.isEnabled()) Sound.CLICK.play();
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
                    // TODO CREATE A CallListenerThread; ADD AN OBSERVER
                    try {
                        callListener = new CallListener(localNickText.getText());
                        callThread = new CallListenerThread(callListener);
                        callThread.addObserver(new Observer() {
                            @Override
                            /*
                            TODO AT THIS RATE INCOMING CONNECTION IS HANDLING IN MAINFORM CLASS
                            TODO IT SHOULD BE HANDLED IN CallListenerThread CLASS
                             */
                            public void update(Observable o, Object arg) {
                                connection = callThread.getLastRequest();
                                connection.sendNickHello(VER, localNickText.getText());
                                Command greetings = connection.receive();
                                if (greetings instanceof NickCommand) {
                                    AskWindow askWindow = new AskWindow();
                                } else {
                                    connection.reject();
                                    try {
                                        connection.close();
                                        disconnecting();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    } catch (InterruptedException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }
                        });
                        Thread t2 = new Thread(callThread);
                        t2.start();
                    } catch (UnknownHostException e5) {
                        e5.printStackTrace();
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
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

    //    Method was created to avoid copying the code
    public void connecting() {
        connect.setEnabled(false);
        remoteAddressText.setEnabled(false);
        newMsg.setEditable(true);
        disconnect.setEnabled(true);
    }

    //    Method was created to avoid copying the code
    public void disconnecting() throws InterruptedException {
        disconnect.setEnabled(false);
        connect.setEnabled(true);
        remoteAddressText.setEnabled(true);
        newMsg.setEditable(false);
        messagingArea.setText("");
        remoteAddressText.setText("");
        remoteNickText.setText("");
        if (commandThread != null)
            commandThread.stop();
        if (callThread != null)
            callThread.setBusy(false);
    }

    //    Method was created to avoid copying the code
    public void listenCommands() {
        if (commandThread != null)
            try {
                commandThread.stop();
            } catch (InterruptedException e) {
                // TODO HANDLE EXCEPTION
                e.printStackTrace();
            }
        commandThread = new CommandListenerThread(connection);
        commandThread.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (commandThread.getLastCommand() != null) {
                    if (commandThread.getLastCommand() instanceof MessageCommand) {
                        Sound.INCOMING.play();
                        messagingArea.append(remoteNickText.getText() + " : " + commandThread.getMessage() + "\n");
                    } else {
                        switch (commandThread.getLastCommand().type) {
                            case ACCEPT: {
                                messagingArea.append(commandThread.getLastCommand().toString() + "\n");
                                break;
                            }
                            case REJECT: {
                                messagingArea.append(commandThread.getLastCommand().toString() + "\n");
                                try {
                                    disconnecting();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case DISCONNECT: {
                                messagingArea.append("Disconnected" + "\n");
                                try {
                                    disconnecting();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }
                    }
                } else {
                    messagingArea.append("Wrong command received. Disconnected");
                    try {
                        disconnecting();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainForm mainForm = new MainForm();
                mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainForm.setVisible(true);
            }
        });

    }
}
