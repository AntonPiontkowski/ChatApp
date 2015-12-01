import java.util.ArrayList;

public class ContactsServer {


    public static ArrayList<Contact> readServer(ServerConnection db){
        String[] users = db.getAllNicks();
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < users.length; i++){
            contacts.add(new Contact(users[i], db.getIpForNick(users[i]), db.isNickOnline(users[i])));
        }
        return contacts;
    }

}
