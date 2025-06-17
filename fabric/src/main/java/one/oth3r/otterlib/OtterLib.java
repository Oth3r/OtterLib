package one.oth3r.otterlib;

import net.fabricmc.api.ModInitializer;

public class OtterLib implements ModInitializer {
    protected static boolean isClient = false;
    /**
     * gets if the loader is clientside or not
     */
    public static boolean isClient() {
        return isClient;
    }

    @Override
    public void onInitialize() {
    }
}
