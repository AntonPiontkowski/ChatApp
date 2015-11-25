import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

/*

CLASS THAT CONTROLS THE SET

 */

public class Application {
    private Connection connection;
    private Caller caller;
    private CallListenerThread callListenerThread;
    private CommandListenerThread commandListenerThread;
    private GUI frame;

    public Application(GUI frame){
        this.frame = frame;
        this.frame.addWindowStateListener(new StateListener());
        this.frame.addSendListener(new SendListener());
        this.frame.addApplyListener(new BtnApplyListener());
        this.frame.addConnectListener(new BtnConListener());
        this.frame.addDisconnectListener(new BtnDisconListener());
        this.frame.addNickKeyListener(new NickTextListener());
        this.frame.addAddrKeyListener(new RemoteAddressListener());
        this.frame.addMsgKeyListener(new WriteMsgListener());
        this.frame.addComponentListener(new ResizeListener());
    }



    public void acceptIncomingCall(){
        this.frame.setRemoteAddress(connection.getSocketAddress().toString());
        this.frame.setRemoteNick(((NickCommand) commandListenerThread.getLastCommand()).getNick());
        this.connection.send(Command.CommandType.ACCEPT.toString());
        this.frame.setConnected();
        this.callListenerThread.setBusy(true);
        commandListenerThread = new CommandListenerThread(connection);
        addCommandObserver();
        commandListenerThread.start();
    }
    public void rejectIncomingCall() throws IOException{
        this.connection = callListenerThread.getLastCon();
        this.connection.send(Command.CommandType.REJECT.toString());
        this.commandListenerThread.stop();
        this.callListenerThread.setBusy(false);
        this.connection.close();
    }

    public void addCommandObserver(){
        this.commandListenerThread.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (commandListenerThread.getLastCommand() != null){
                    if (commandListenerThread.getLastCommand() instanceof NickCommand){
                        Sounds.INCOMING.play();
                        frame.incomingCall(((NickCommand) commandListenerThread.getLastCommand()).getNick());
                        frame.addAcceptListener(new AcceptListener());
                        frame.addRejectListener(new RejectListener());
                        commandListenerThread.stop();
                    }
                    else if (commandListenerThread.getLastCommand() instanceof MessageCommand){
                        Sounds.RECEIVE.play();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                frame.appendMsg(((MessageCommand) commandListenerThread.getLastCommand()).getMessage());
                            }
                        });
                    }
                    else if (Checker.getType(commandListenerThread.getLastCommand().getType().toString()) != null){
                        switch (commandListenerThread.getLastCommand().getType()){
                            case ACCEPT:{
                                callListenerThread.setBusy(true);
                                break;
                            }
                            case REJECT:{
                                callListenerThread.setBusy(false);
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        frame.appendBroken("REJECTED");
                                        frame.setDisconnected();
                                    }
                                });
                                break;
                            }
                            case DISCONNECT:{
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        frame.appendBroken("DISCONNECTED");
                                        frame.setDisconnected();
                                    }
                                });
                                callListenerThread.setBusy(false);
                                commandListenerThread = null;
                                connection = null;
                                break;
                            }
                        }
                    }
                }
                else {
                    commandListenerThread.stop();
                    connection = null;
                    commandListenerThread = null;
                    callListenerThread.setBusy(false);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            frame.appendBroken("CONNECTION BROKEN");
                            frame.setDisconnected();
                        }
                    });
                }
            }
        });
    }

    // Listeners
    private class StateListener implements WindowStateListener{
        @Override
        public void windowStateChanged(WindowEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setLocations();
                }
            });
        }
    }
    private class ResizeListener extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setLocations();
                }
            });

        }
    }
    private class SendListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (frame.sendIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String msg = frame.getMsg();
                        if (msg.length() > 0){
                            frame.appendMyMsg(msg);
                            Sounds.SEND.play();
                            connection.sendMsg(msg);
                        }
                    }
                });
            else
                Sounds.DISABLED.play();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.sendIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setSendIcon(new ImageIcon(getClass().getResource("gui/frame/sendPrs.png")));
                    }
                });
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frame.sendIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setSendIcon(new ImageIcon(getClass().getResource("gui/frame/sendEnt.png")));
                    }
                });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frame.sendIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setSendIcon(new ImageIcon(getClass().getResource("gui/frame/sendEnt.png")));
                    }
                });
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frame.sendIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setSendIcon(new ImageIcon(getClass().getResource("gui/frame/sendIcon.png")));
                    }
                });
        }
    }
    private class BtnConListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (frame.conIsEnabled()){
            // TODO CONNECTING
                if (frame.getRemoteAddress().length() > 3){
                    caller.setRemoteAddress(new InetSocketAddress(frame.getRemoteAddress(),Constants.PORT));
                    connection = caller.call();
                    if (connection == null){
                        // TODO WRITE CALLSTATUS TO THE TEXT FIELD OR SHOW A MESSAGE WITH CALL STATUS
                    }
                    else{
                        commandListenerThread = new CommandListenerThread(connection);
                        addCommandObserver();
                        frame.setRemoteNick(caller.getRemoteNick());
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                frame.setConnected();
                            }
                        });
                        commandListenerThread.start();
                        addCommandObserver();
                    }
                }
                else {
                    // TODO SHOW A MESSAGE TO FILL THE REMOTE ADDRESS FIELD
                }
            }
            else
                Sounds.DISABLED.play();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.conIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setConnectIcon(new ImageIcon(getClass().getResource("gui/frame/conPrs.png")));
                    }
                });
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frame.conIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setConnectIcon(new ImageIcon(getClass().getResource("gui/frame/conEnt.png")));
                    }
                });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frame.conIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setConnectIcon(new ImageIcon(getClass().getResource("gui/frame/conEnt.png")));
                    }
                });
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frame.conIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setConnectIcon(new ImageIcon(getClass().getResource("gui/frame/conIcon.png")));
                    }
                });
        }
    }
    private class BtnApplyListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (frame.applyIsEnabled()){
                // TODO CREATING Caller, STARTING CallListenerThread
                if (frame.getLocalNick().length() > 3 | frame.getLocalNick().length() == 0){
                    try{
                        if (frame.getLocalNick().length() == 0)
                            frame.setLocalNick(Constants.DEFAULT_NAME);
                        caller = new Caller(frame.getLocalNick());
                        callListenerThread = new CallListenerThread(new CallListener(frame.getLocalNick()));
                        callListenerThread.addObserver(new Observer() {
                            @Override
                            public void update(Observable o, Object arg) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        connection = callListenerThread.getLastCon();
                                        commandListenerThread = new CommandListenerThread(connection);
                                        addCommandObserver();
                                        commandListenerThread.start();
                                    }
                                });
                            }
                        });
                        Thread t = new Thread(callListenerThread);
                        t.setDaemon(true);
                        t.start();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                frame.setApplied();
                            }
                        });

                    }

                    catch (UnknownHostException e2){
                        e2.printStackTrace();
                    }
                    catch (IOException e3){
                        e3.printStackTrace();
                    }
               }
                else {
                    // TODO SHOW MESSSAGE THAT LENGTH SHOULD BE MORE THAN 3 CHARS
                }
            }
            else {
                Sounds.DISABLED.play();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.applyIsEnabled())
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setApplyIcon(new ImageIcon(getClass().getResource("gui/frame/applyPrs.png")));
                }
            });
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frame.applyIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setApplyIcon(new ImageIcon(getClass().getResource("gui/frame/applyEnt.png")));
                    }
                });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frame.applyIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setApplyIcon(new ImageIcon(getClass().getResource("gui/frame/applyEnt.png")));
                    }
                });
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frame.applyIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setApplyIcon(new ImageIcon(getClass().getResource("gui/frame/applyIcon.png")));
                    }
                });
        }
    }
    private class BtnDisconListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (frame.disconIsEnabled()){
                // TODO DISCONNECTING
                commandListenerThread.stop();
                connection.send(Command.CommandType.DISCONNECT.toString());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setDisconnected();
                    }
                });
            }
            else
                Sounds.DISABLED.play();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.disconIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setDisconnectIcon(new ImageIcon(getClass().getResource("gui/frame/disconPrs.png")));
                    }
                });
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frame.disconIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setDisconnectIcon(new ImageIcon(getClass().getResource("gui/frame/disconEnt.png")));
                    }
                });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frame.disconIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setDisconnectIcon(new ImageIcon(getClass().getResource("gui/frame/disconEnt.png")));
                    }
                });
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frame.disconIsEnabled())
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setDisconnectIcon(new ImageIcon(getClass().getResource("gui/frame/disconIcon.png")));
                    }
                });
        }
    }
    private class AcceptListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            acceptIncomingCall();
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setConnected();
                        frame.dialogSetVisible(false);
                    }
                });
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setAcceptIcon(new ImageIcon(getClass().getResource("gui/dialog/acPrs.png")));
                    }
                });
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setAcceptIcon(new ImageIcon(getClass().getResource("gui/dialog/acEnt.png")));
                    }
                });
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setAcceptIcon(new ImageIcon(getClass().getResource("gui/dialog/acEnt.png")));
                    }
                });
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setAcceptIcon(new ImageIcon(getClass().getResource("gui/dialog/acIcon.png")));
                    }
                });
            }
        }
    }
    private class RejectListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (frame.incomingVisible()){
                try{
                    rejectIncomingCall();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            frame.dialogSetVisible(false);
                        }
                    });
                }
                catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setRejectIcon(new ImageIcon(getClass().getResource("gui/dialog/rejPrs.png")));
                    }
                });
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setRejectIcon(new ImageIcon(getClass().getResource("gui/dialog/rejEnt.png")));
                    }
                });
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setRejectIcon(new ImageIcon(getClass().getResource("gui/dialog/rejEnt.png")));
                    }
                });
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (frame.incomingVisible()){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setRejectIcon(new ImageIcon(getClass().getResource("gui/dialog/rejIcon.png")));
                    }
                });
            }
        }
    }
    private class NickTextListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            StringBuilder b = new StringBuilder(frame.getLocalNick());
            if (b.length() > Constants.NICK_LENGTH_MAX) {
                frame.setLocalNick(b.delete(Constants.NICK_LENGTH_MAX, b.length()).toString());
            }
            if (e.getKeyChar() == '\n') {
                frame.setLocalNick(b.toString().trim());
            }
        }
    }
    private class RemoteAddressListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    private class WriteMsgListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == '\n'){
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String msg = frame.getMsg().split("\n")[0];
                        if (msg.length() > 0){
                            frame.appendMyMsg(msg);
                            Sounds.SEND.play();
                            connection.sendMsg(msg);
                        }
                    }
                });
            }
        }
    }
}
