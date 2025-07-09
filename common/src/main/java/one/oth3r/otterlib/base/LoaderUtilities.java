package one.oth3r.otterlib.base;

import one.oth3r.otterlib.chat.CTxT;

public interface LoaderUtilities {
    /**
     * gets the logger for the current OtterLib instance
     */
    OtterLogger getLogger();

    /**
     * Trys to convert the object into a CTxT. <br/>
     * on different loaders, different tactics are used to grab the loader's different default Text implementations
     * @return the CTxT
     */
    CTxT getCTxTFromObj(Object obj);

    /**
     * a method to check if OtterLib is running on the client or not
     */
    boolean isClient();

    /**
     * a method to check if OtterLib is initialized or not <br/>
     * used to check if a newly registered file should be loaded or wait for initialization to do it automatically
     */
    boolean isInitialized();

    /**
     * only works on client* <br/>
     * gets the localized text using the client side language system
     */
    CTxT getClientTranslatable(String key, Object... args);
}
