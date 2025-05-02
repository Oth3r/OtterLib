package one.oth3r.otterlib.client.screen.utl;

public class ScreenUtl {
    private static boolean focused = false;

    public static boolean isFocused() {
        return focused;
    }

    public static void setFocused(boolean focused) {
        ScreenUtl.focused = focused;
    }
}
