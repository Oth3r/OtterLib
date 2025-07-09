package one.oth3r.otterlib.file;

import java.util.HashMap;
import java.util.Map;

public class LangRegistry {
    private static final String DEFAULT_LANG_ID = "main";
    private static final Map<String, Map<String, LanguageReader>> REGISTRY = new HashMap<>();

    /**
     * Registers a LanguageReader to the registry.
     * @param modId the id of the mod to register, e.g. "examplemod"
     * @param langId the id of the language to register, e.g. "player_oth3r"
     * @param lang the LanguageReader instance to register
     * @return true if registration was successful, false otherwise
     */
    public static boolean registerLang(String modId, String langId, LanguageReader lang) {
        Map<String, LanguageReader> modLangs = REGISTRY.computeIfAbsent(modId, k -> new HashMap<>());
        boolean isNew = !modLangs.containsKey(langId) || modLangs.get(langId) != lang;
        modLangs.put(langId, lang);
        return isNew;
    }

    /**
     * Registers a LanguageReader to the registry with the default "main" language id.
     * @param modId the id of the mod to register
     * @param lang the LanguageReader instance to register
     * @return true if registration was successful, false otherwise
     */
    public static boolean registerLang(String modId, LanguageReader lang) {
        return registerLang(modId, DEFAULT_LANG_ID, lang);
    }

    /**
     * Unregisters a LanguageReader from the registry.
     * @param modId the id of the mod to unregister
     * @param langId the id of the language to unregister
     * @return true if successful, false if not
     */
    public static boolean unregisterLang(String modId, String langId) {
        Map<String, LanguageReader> modLangs = REGISTRY.get(modId);
        if (modLangs != null) {
            return modLangs.remove(langId) != null;
        }
        return false;
    }

    /**
     * Unregisters a LanguageReader from the registry with the default "main" language id.
     * @param modId the id of the mod to unregister
     * @return true if successful, false if not
     */
    public static boolean unregisterLang(String modId) {
        return unregisterLang(modId, DEFAULT_LANG_ID);
    }

    /**
     * Gets a LanguageReader from the registry using the mod id and language id.
     * @param modId the id of the mod
     * @param langId the id of the language
     * @return the LanguageReader for the given mod and language id, or null if not found
     */
    public static LanguageReader getLang(String modId, String langId) {
        Map<String, LanguageReader> modLangs = REGISTRY.get(modId);
        if (modLangs != null) {
            return modLangs.get(langId);
        }
        return null;
    }

    /**
     * Gets a LanguageReader from the registry using the mod id and the default "main" language id.
     * @param modId the id of the mod
     * @return the LanguageReader for the given mod and "main" language id, or null if not found
     */
    public static LanguageReader getLang(String modId) {
        return getLang(modId, DEFAULT_LANG_ID);
    }

    /**
     * Checks if a LanguageReader is registered for the given mod id and language id.
     * @param modId the id of the mod
     * @param langId the id of the language
     * @return true if the language is registered, false otherwise
     */
    public static boolean hasLang(String modId, String langId) {
        Map<String, LanguageReader> modLangs = REGISTRY.get(modId);
        return modLangs != null && modLangs.containsKey(langId);
    }

    /**
     * Checks if a LanguageReader is registered for the given mod id and the default "main" language id.
     * @param modId the id of the mod
     * @return true if the language is registered, false otherwise
     */
    public static boolean hasLang(String modId) {
        return hasLang(modId, DEFAULT_LANG_ID);
    }
}
