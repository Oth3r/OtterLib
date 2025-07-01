package one.oth3r.otterlib;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import one.oth3r.otterlib.file.FileRegistry;

public class OtterLibServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            FileRegistry.finishRegistration();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            FileRegistry.saveAllFiles();
        });
    }
}
