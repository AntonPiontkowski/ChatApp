import javax.swing.*;
import java.io.File;

public class FileManager {

    private File file;

    public FileManager() {
    }


    public void setFile() {
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Open File");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
        }
    }


}
