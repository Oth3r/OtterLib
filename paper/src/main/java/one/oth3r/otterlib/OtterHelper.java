package one.oth3r.otterlib;

import net.kyori.adventure.text.TextComponent;
import one.oth3r.otterlib.base.LoaderUtilities;
import one.oth3r.otterlib.base.OtterLogger;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.LoaderText;
import one.oth3r.otterlib.chat.Wrapper;

public class OtterHelper implements LoaderUtilities {
    /**
     * gets the logger for the current OtterLib instance
     */
    @Override
    public OtterLogger getLogger() {
        return new OtterLogger("OtterLib");
    }

    /**
     * Trys to convert the object into a CTxT. <br/>
     * on different loaders, different tactics are used to grab the loader's different default Text implementations
     *
     * @param obj the obj to try to convert
     * @return the CTxT
     */
    @Override
    public CTxT getCTxTFromObj(Object obj) {
        return switch (obj) {
            case CTxT txt -> txt.clone();
            case LoaderText<?> txt -> new CTxT(txt.b());
            case TextComponent textComponent -> new CTxT(textComponent);
            // else, try to convert into a string
            case null, default -> new CTxT(String.valueOf(obj));
        };
    }

    /**
     * a method to check if OtterLib is running on the client or not
     */
    @Override
    public boolean isClient() {
        return false;
    }

    /**
     * a method to check if OtterLib is initialized or not <br/>
     * used to check if a newly registered file should be loaded or wait for initialization to do it automatically
     */
    @Override
    public boolean isInitialized() {
        return OtterLib.isInitialized();
    }

    /**
     * only works on client* <br/>
     * gets the localized text using the client side language system
     */
    @Override
    public CTxT getClientTranslatable(String key, Object... args) {
        return null;
    }

    /**
     * Gets the default wrapper for the ChatText system.
     *
     * @return the default Wrapper
     */
    @Override
    public Wrapper<?, ?> getDefaultWrapper() {
        return new Wrapper<>(new CTxT("["),new CTxT("]"));
    }
}
