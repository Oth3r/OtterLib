package one.oth3r.otterlib.chat.hover;

import one.oth3r.otterlib.chat.LoaderText;

public abstract class HoverAction<A> {
    /**
     * gets the object that this action will use.
     * @return the action object
     */
    public abstract A getActionObject();

    /**
     * sets the object that this action will use.
     * @param actionObject the action object
     */
    public abstract void setActionObject(A actionObject);

    /**
     * creates a hover action from an object. the hover action will be created based on the type of the object.
     * @param o the object to create the hover action from
     * @return the hover action
     */
    @SuppressWarnings("unchecked")
    public static <T extends LoaderText<T>> HoverAction<?> of(Object o) {
        if (o instanceof LoaderText<?>) {
            return new HoverTxT<>((T) o);
        } else {
            //todo make a loader util method that handles the rest of the hover actions
            return null;
        }
    }
}
