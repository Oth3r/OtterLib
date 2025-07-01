package one.oth3r.otterlib.file;

import java.util.HashMap;
import java.util.Map;

public class FileRegistry {
    private static final Map<String, Map<String, FileEntry>> REGISTRY = new HashMap<>();

    /**
     * Registers a CustomFile to the registry. <br/>
     * This method should be called during mod initialization to easily register files for OtterLib to manage.
     * @param modId the mod id that registered the file
     * @param entry the FileEntry for easy registration
     */
    public static void registerFile(String modId, FileEntry entry) {
        REGISTRY.computeIfAbsent(modId, id -> new HashMap<>()).put(entry.fileId,entry);
    }

    /**
     * Gets the CustomFile class using the modId and fileId.
     * @param modId the mod id that registered the file
     * @param fileId the id of the file registered
     */
    public static CustomFile<?> getFile(String modId, String fileId) {
        Map<String, FileEntry> files = REGISTRY.get(modId);
        FileEntry entry = (files != null) ? files.get(fileId) : null;
        return entry == null ? null : entry.file();
    }

    /**
     * Loads all files that have {@link FileEntry#autoLoad} enabled. <br/>
     * Automatically called by OtterLib on ServerStartedEvent or ClientStartedEvent (if on client) & `/reload` command on modded loaders.
     */
    public static void finishRegistration() {
        REGISTRY.values().forEach(map -> map.values().forEach(entry -> {
            if (entry.autoLoad()) entry.file().load();
        }));
    }

    /**
     * Saves all files that have {@link FileEntry#saveOnStop} enabled. <br/>
     * Automatically called by OtterLib on ServerStoppingEvent or ClientStoppingEvent (if on client).
     */
    public static void saveAllFiles() {
        REGISTRY.values().forEach(map -> map.values().forEach(entry -> {
            if (entry.saveOnStop()) entry.file().save();
        }));
    }

    /**
     * Holds metadata for a custom file registered by a mod.
     */
    public record FileEntry(String fileId, CustomFile<?> file, boolean autoLoad, boolean saveOnStop) {}
}
