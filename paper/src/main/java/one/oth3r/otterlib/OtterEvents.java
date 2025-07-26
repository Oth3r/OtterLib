package one.oth3r.otterlib;

import one.oth3r.otterlib.registry.CustomFileReg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class OtterEvents implements Listener {
    @EventHandler
    public static void serverLoadEvent(ServerLoadEvent event) {
        // this is called when the server is loaded, so we can queue file loading here
        CustomFileReg.queueLoading();

        // finish initialization
        OtterLib.isInitialized = true;
    }
}
