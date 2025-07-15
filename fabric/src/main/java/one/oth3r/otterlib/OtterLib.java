package one.oth3r.otterlib;

import net.fabricmc.api.ModInitializer;

public class OtterLib implements ModInitializer {
    protected static boolean isClient = false;
    protected static boolean isInitialized = false;
    /**
     * gets if the loader is clientside or not
     */
    public static boolean isClient() {
        return isClient;
    }

    /**
     * gets if OtterLib is initialized or not
     */
    public static boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void onInitialize() {}
}
