import javax.swing.*;

<<<<<<< HEAD
public class Contact{
=======
public class Contact {
>>>>>>> c4521e745c4f4e010ce3fede0da9ff0eaf5fdd94
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
