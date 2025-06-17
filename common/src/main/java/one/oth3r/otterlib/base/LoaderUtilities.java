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
}
