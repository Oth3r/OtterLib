package one.oth3r.otterlib.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import one.oth3r.otterlib.Assets;
import one.oth3r.otterlib.chat.CTxT;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LanguageReader {
    private final ResourceReader defaultResource;
    /// null if no override resource is provided, meaning no alternate config language location
    private final ResourceReader overrideResource;
    private final String defaultLanguage;

    private final Map<String, String> defaultLangMap = new HashMap<>();
    private final Map<String, String> languageMap = new HashMap<>();

    private String currentLanguage;

    /**
     * creates a LanguageReader. all language files are parsed in the `.json` format
     * @param defaultResource the path for all base language files
     * @param overrideResource the path for override languages (player specified config languages)
     * @param defaultLanguage the key for the default language (e.g., en_us)
     * @param currentLanguage the key for the current language (e.g., en_us)
     */
    public LanguageReader(ResourceReader defaultResource, ResourceReader overrideResource, String defaultLanguage, String currentLanguage) {
        // use a classloader instead of path
        // maybe make a custom class for this instead
        this.defaultResource = defaultResource;
        this.overrideResource = overrideResource;
        this.defaultLanguage = defaultLanguage;
        this.currentLanguage = currentLanguage;

        // load
        load();
    }

    /**
     * creates a LanguageReader. all language files are parsed in the `.json` format
     * @param defaultResource the path for all base language files
     * @param defaultLanguage the key for the default language (e.g., en_us)
     * @param currentLanguage the key for the current language (e.g., en_us)
     */
    public LanguageReader(ResourceReader defaultResource, String defaultLanguage, String currentLanguage) {
        this.defaultResource = defaultResource;
        this.overrideResource = null;
        this.defaultLanguage = defaultLanguage;
        this.currentLanguage = currentLanguage;

        // load
        load();
    }

    /**
     * updates the current language of the reader, and reloads the languages
     * @param newLanguage the new language to load (the name of the file)
     */
    public void updateLanguage(String newLanguage) {
        currentLanguage = newLanguage;
        load();
    }

    /**
     * reloads the language files
     */
    public void reloadLanguages() {
        load();
    }

    /**
     * loads the language files needed for the reader to work
     */
    private void load() {
        Type tToken = new TypeToken<Map<String, String>>(){}.getType();
        // load the languageMap
        try {
            languageMap.putAll(new Gson().fromJson(getBufferedReader(false), tToken));
        } catch (Exception e) {
            Assets.HELPER.getLogger().error("Error loading language file! "+e);
        }

        // also load the default language as a fallback
        try {
            defaultLangMap.putAll(new Gson().fromJson(getBufferedReader(true), tToken));
        } catch (Exception e) {
            Assets.HELPER.getLogger().error("Error loading default language file! "+e);
        }
    }

    /**
     * gets the {@link BufferedReader} based on importance <br/>
     * 1. override path language first if available <br/>
     * 2. default path language <br/>
     * 3. default path, default language
     * @param getDefault if enabled, will skip to step 3 and get the default language, default path
     * @return the correct reader
     * @throws IOException if the file cannot be read / I/O issues
     * @throws IllegalArgumentException if the default path, default language cannot be read (very bad)
     */
    private BufferedReader getBufferedReader(boolean getDefault) throws IOException {
        if (!getDefault) {
            if (overrideResource != null) {
                try {
                    // return the reader if the override language is available
                    return overrideResource.getResource(currentLanguage+".json");
                }
                // if exception is thrown, it means the override language is not available
                catch (Exception ignored) {}
            }
            try {
                return defaultResource.getResource(currentLanguage+".json");
            } catch (Exception e) {
                Assets.HELPER.getLogger().error("Error loading language file `%s` from: "+defaultResource.toString(), currentLanguage);
            }
        }
        return defaultResource.getResource(defaultLanguage+".json");
    }

    /**
     * tries to get the language value using the key. <br/>
     * falls back to the default map if there isn't a key in the language map
     * @param key the key to search
     * @return the value from the key provided
     */
    private String getLanguageValue(String key) {
        return languageMap.getOrDefault(key, defaultLangMap.getOrDefault(key, key));
    }

    /**
     * retrieves a translation from the client, if on the client, and from the loaded translation if not (useful for client sided text and UI)
     * @param key the translation key
     * @param args the arguments for the translation
     * @return the CTxT of the translation
     */
    public CTxT dynamicTranslatable(String key, Object... args) {
        if (Assets.HELPER.isClient()) // client side, should attempt to use the client's language - might be different from the config language
            return Assets.HELPER.getClientTranslatable(key, args);
        else // not client side
            return translatable(key, args);
    }

    /**
     * retrieves a translation from the currently loaded language <br/>
     * if the key doesn't exist, the default language file will also be checked.
     * @param key the translation key
     * @param args the arguments for the translation
     * @return the CTxT of the translation
     */
    public CTxT translatable(String key, Object... args) {
        return new Parser(key,args).getCTxT();
    }

    /**
     * Parses the key-arg into a formatted CTxT
     */
    private class Parser {
        private final String translationKey;
        private final Object[] placeholders;

        public Parser(String translationKey, Object... placeholders) {
            this.translationKey = translationKey;
            this.placeholders = placeholders;
        }

        public CTxT getCTxT() {
            String translated = getLanguageValue(translationKey);
            if (placeholders != null && placeholders.length > 0) {
                //removed all double \\ and replaces with \
                translated = translated.replaceAll("\\\\\\\\", "\\\\");
                String regex = "%\\d*\\$?[dfs]";
                Matcher anyMatch = Pattern.compile(regex).matcher(translated);
                Matcher endMatch = Pattern.compile(regex + "$").matcher(translated);

                // Arraylist with all the %(#$)[dfs]
                ArrayList<String> matches = new ArrayList<>();
                while (anyMatch.find()) {
                    String match = anyMatch.group();
                    matches.add(match);
                }
                //SPLITS the text at each regex and remove the regex
                String[] parts = translated.split(regex);
                //if the last element of the array ends with regex, remove it and add an empty string to the end of the array
                if (endMatch.find()) {
                    String[] newParts = Arrays.copyOf(parts, parts.length + 1);
                    newParts[parts.length] = "";
                    parts = newParts;
                }
                //if there are placeholders specified, and the split is more than 1, it will replace %(dfs) with the placeholder objects
                if (parts.length > 1) {
                    CTxT txt = new CTxT();
                    int i = 0;
                    for (String match : matches) {
                        int get = i;
                        //if the match is numbered, change GET to the number it wants
                        if (match.contains("$")) {
                            match = match.substring(1, match.indexOf('$'));
                            get = Integer.parseInt(match) - 1;
                        }
                        if (parts.length != i) txt.append(parts[i]);
                        //convert the obj into txt
                        txt.append(Assets.HELPER.getCTxTFromObj(placeholders[get]));
                        i++;
                    }
                    if (parts.length != i) txt.append(parts[i]);
                    return new CTxT(txt);
                }
            }
            return new CTxT(translated);
        }
    }
}