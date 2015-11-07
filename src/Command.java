public class Command {

    public CommandType type;

    public Command() {
    }

    public Command(CommandType t) {
        this.type = t;
    }

    public static CommandType getType(String name) {
        CommandType[] commands = CommandType.values();
        CommandType rightType = null;
        for (CommandType types : commands) {
            if (types.toString().equals(name)) {
                rightType = types;
                break;
            }
        }
        return rightType;
    }

    public enum CommandType {
        ACCEPT {
            @Override
            public String toString() {
                return "Accepted";
            }
        },

        DISCONNECT {
            @Override
            public String toString() {
                return "Disconnect";
            }
        },
        MESSAGE {
            @Override
            public String toString() {
                return "Message";
            }
        },
        NICK {
            @Override
            public String toString() {
                return "Nick";
            }
        },
        REJECT {
            @Override
            public String toString() {
                return "Rejected";
            }
        }
    }
}