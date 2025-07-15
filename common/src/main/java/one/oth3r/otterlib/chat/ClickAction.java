package one.oth3r.otterlib.chat;

import one.oth3r.otterlib.base.Enums;

public class ClickAction {
    protected final ClickActions action;
    protected final String actionString;

    public ClickAction(ClickActions action, String actionString) {
        this.action = action;
        this.actionString = actionString;
    }

    public ClickAction(int action, String actionString) {
        this.action = Enums.search((e) -> e.getCode() == action, ClickActions.class).stream()
                .findFirst().orElse(ClickActions.NOTHING);
        this.actionString = actionString;
    }

    public ClickAction(ClickAction action) {
        this.action = action.action;
        this.actionString = action.actionString;
    }

    public ClickActions getAction() {
        return action;
    }

    public String getActionString() {
        return actionString;
    }
}
