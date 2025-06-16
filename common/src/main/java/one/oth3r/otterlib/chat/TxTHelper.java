package one.oth3r.otterlib.chat;

import one.oth3r.otterlib.file.LanguageReader;

public interface TxTHelper {
    /**
     * gets the language reader for this TxT Helper instance
     * @return the language reader
     */
    LanguageReader getLanguageReader();

    /**
     * creates a chat tag, e.g., [OtterLib]
     */
    CTxT getChatTag();

    /**
     * retrieves the language entry using {@link #getLanguageReader()}, just a shorter method, nothing fancy
     */
    default CTxT lang(String key, Object... args) {
        return getLanguageReader().get(key, args);
    }
}
