package one.oth3r.otterlib.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import one.oth3r.otterlib.Assets;
import one.oth3r.otterlib.chat.CTxT;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LanguageReader {
    private final Path defaultPath;
    private final Path overridePath;
    private final String defaultLanguage;

    private final Map<String, String> defaultLangMap = new HashMap<>();
    private final Map<String, String> languageMap = new HashMap<>();

    private String currentLanguage;

    /**
     * creates a LanguageReader. all language files are parsed in the `.json` format
     * @param defaultPath the path for all base language files
     * @param overridePath the path for override languages (player specified config languages)
     * @param defaultLanguage the key for the default language (e.g., en_us)
     * @param currentLanguage the key for the current language (e.g., en_us)
     */
    public LanguageReader(Path defaultPath, Path overridePath, String defaultLanguage, String currentLanguage) {
        this.defaultPath = defaultPath;
        this.overridePath = overridePath;
        this.defaultLanguage = defaultLanguage;
        this.currentLanguage = currentLanguage;

        // load
        load();
    }

    /**
     * creates a LanguageReader. all language files are parsed in the `.json` format
     * @param defaultPath the path for all base language files
     * @param defaultLanguage the key for the default language (e.g., en_us)
     * @param currentLanguage the key for the current language (e.g., en_us)
     */
    public LanguageReader(Path defaultPath, String defaultLanguage, String currentLanguage) {
        this.defaultPath = defaultPath;
        this.overridePath = Path.of("");
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
            Assets.HELPER.getLogger().severe("Error loading language file! "+e);
        }

        // also load the default language as a fallback
        try {
            defaultLangMap.putAll(new Gson().fromJson(getBufferedReader(true), tToken));
        } catch (Exception e) {
            Assets.HELPER.getLogger().severe("Error loading default language file! "+e);
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
    private BufferedReader getBufferedReader(boolean getDefault) throws IOException, IllegalArgumentException {
        Path path;
        if (!getDefault) {
            // first test, override path + current language
            path = Paths.get(overridePath.toString(), currentLanguage);
            // if failed, default path + current language
            if (!Files.exists(path)) {
                path = Paths.get(defaultPath.toString(), currentLanguage);
                // if failed, full default
                if (!Files.exists(path)) return getBufferedReader(true);
            }
        } else {
            path = Paths.get(defaultPath.toString(), defaultLanguage);
            if (!Files.exists(path)) throw new IllegalArgumentException(
                    String.format("The default language at `%s` doesn't exist!",path));
        }

        return Files.newBufferedReader(path, StandardCharsets.UTF_8);
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
     * retrieves a translation from the currently loaded language <br/>
     * if the key doesn't exist, the default language file will also be checked.
     * @param key the translation key
     * @param args the arguments for the translation
     * @return the CTxT of the translation
     */
    public CTxT get(String key, Object... args) {
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