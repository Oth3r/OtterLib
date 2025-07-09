package one.oth3r.otterlib;

import org.bukkit.plugin.java.JavaPlugin;

public class OtterLib extends JavaPlugin {
    protected static boolean isInitialized = false;

    public static boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new OtterEvents(), this);
    }

    @Override
    public void onDisable() {}
}
