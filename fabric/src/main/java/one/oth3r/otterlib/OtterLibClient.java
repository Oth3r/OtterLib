package one.oth3r.otterlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import one.oth3r.otterlib.registry.CustomFileReg;

public class OtterLibClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        OtterLib.isClient = true;

        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            CustomFileReg.queueLoading();
            // finish initialization
            OtterLib.isInitialized = true;
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
            CustomFileReg.saveAllFiles();
        });
    }
}
