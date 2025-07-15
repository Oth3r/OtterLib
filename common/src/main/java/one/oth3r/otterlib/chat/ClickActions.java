package one.oth3r.otterlib.chat;

public enum ClickActions {
    NOTHING("nothing", 0),
    RUN_COMMAND("run_command", 1),
    SUGGEST_COMMAND("suggest_command",2),
    OPEN_URL("open_url",3),
    OPEN_FILE("open_file",4),
    COPY_TO_CLIPBOARD("copy_to_clipboard",5),
    CHANGE_PAGE("change_page",6);

    private final String name;
    private final int code;

    ClickActions(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
