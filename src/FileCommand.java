public class FileCommand extends Command {
    private String fileName;

    public FileCommand() {
    }

    public FileCommand(String file) {
        this.fileName = file;
    }

    public void setFile(String message) {
        this.fileName = message;
    }

    public String getFile() {
        return this.fileName;
    }
}
