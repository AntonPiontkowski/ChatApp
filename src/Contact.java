import javax.swing.*;

public class Contact{
    private String nick;
    private String addr;
    private boolean isOnline;

    public Contact(String nick, String addr){
        this.nick = nick;
        this.addr = addr;
    }

    public String getNick(){
        return this.nick;
    }
    public boolean getOnline(){
        return this.isOnline;
    }
    public String getAddr(){
        return this.addr;
    }
    public void setNick(String nick){
        this.nick = nick;
    }
    public void setAddr(String addr){
        this.addr = addr;
    }
    public void setOnline(boolean isOnline){
        this.isOnline = isOnline;
    }

}
