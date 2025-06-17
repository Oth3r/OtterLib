package one.oth3r.otterlib;

import net.fabricmc.api.ClientModInitializer;

public class OtterLibClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        OtterLib.isClient = true;
    }
}
