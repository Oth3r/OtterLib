package one.oth3r.otterlib;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import one.oth3r.otterlib.registry.CustomFileReg;

public class OtterLibServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            CustomFileReg.queueLoading();

            // finish initialization
            OtterLib.isInitialized = true;
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            CustomFileReg.saveAllFiles();
        });
    }
}
