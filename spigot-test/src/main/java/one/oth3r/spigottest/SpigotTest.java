package one.oth3r.spigottest;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class SpigotTest extends JavaPlugin {
    public static String configDir = "";

    @Override
    public void onEnable() {
        Logger logger = Logger.getLogger("SPIGOT_TEST_LOG");
        getServer().getPluginManager().registerEvents(new Events(), this);
        configDir = this.getDataFolder().getPath();
        TestFile file = new TestFile();
        file.load();
        logger.info("loaded config");
        file.save();
    }

    @Override
    public void onDisable() {

    }
}
