package one.oth3r.otterlib.chat.hover;

import one.oth3r.otterlib.chat.LoaderText;

public class HoverTxT<T extends LoaderText<T>> extends HoverAction<LoaderText<T>> {
    protected LoaderText<T> text;

    public HoverTxT(LoaderText<T> text) {
        this.text = text.clone();
    }

    @Override
    public LoaderText<T> getActionObject() {
        return text;
    }

    @Override
    public void setActionObject(LoaderText<T> actionObject) {
        text = actionObject.clone();
    }

    public static <T extends LoaderText<T>> HoverTxT<T> of(LoaderText<T> text) {
        return new HoverTxT<>(text);
    }
}
