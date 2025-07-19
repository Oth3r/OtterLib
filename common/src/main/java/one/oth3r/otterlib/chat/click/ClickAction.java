package one.oth3r.otterlib.chat.click;

import one.oth3r.otterlib.base.Enums;

public class ClickAction {
    protected final ClickActions action;
    protected final String actionString;

    /**
     * constructor for action by enum
     * @param action action enum
     * @param actionString string for the action to take
     */
    public ClickAction(ClickActions action, String actionString) {
        this.action = action;
        this.actionString = actionString;
    }

    /**
     * constructor for action by code
     * @param action action code
     * @param actionString string for the action to take
     */
    public ClickAction(int action, String actionString) {
        this.action = Enums.search((e) -> e.getCode() == action, ClickActions.class).stream()
                .findFirst().orElse(ClickActions.NOTHING);
        this.actionString = actionString;
    }

    /**
     * copy constructor
     * @param action action to copy
     */
    public ClickAction(ClickAction action) {
        this.action = action.action;
        this.actionString = action.actionString;
    }

    /**
     * @return the action enum
     */
    public ClickActions getAction() {
        return action;
    }

    /**
     * @return the action string
     */
    public String getActionString() {
        return actionString;
    }

    /**
     * static factory method to create a ClickAction with an enum
     */
    public static ClickAction of(ClickActions action, String actionString) {
        return new ClickAction(action, actionString);
    }

    /**
     * static factory method to create a ClickAction with an action code
     */
    public static ClickAction of(int action, String actionString) {
        return new ClickAction(action, actionString);
    }
}
