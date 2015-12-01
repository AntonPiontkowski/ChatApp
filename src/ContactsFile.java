import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ContactsFile {

    private static ServerConnection db;

    public ArrayList<Contact> readFile(ServerConnection db){
        ArrayList<Contact> contactsList = new ArrayList<>();
        try(Scanner in = new Scanner(new FileInputStream("contacts.txt"),"UTF-8")){
            while (in.hasNextLine()){
                contactsList.add(readContact(in));
            }
            return contactsList;
        }
        catch (FileNotFoundException e){
            File contacts = new File("contacts.txt");
            return null;
        }
    }

    public static void writeFile(ArrayList<Contact> contactsList){
        try(PrintWriter out = new PrintWriter("contacts.txt", "UTF-8")){
            for (int i = 0; i < contactsList.size(); i++){
                writeContact(contactsList.get(i), out);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2){
            e2.printStackTrace();
        }
    }

    public static Contact readContact(Scanner in){
        String userInfo = in.nextLine();
        String[] info = userInfo.split("\\|");
        if (db.isNickOnline(info[0]))
            return new Contact(info[0], info[1], true);
        else
            return new Contact(info[0], info[1], false);
    }

    public static void writeContact(Contact contact, PrintWriter out){
        out.println(contact.getNick() + "|" + contact.getAddr());
    }
}
