package one.oth3r.otterlib;

import net.md_5.bungee.api.chat.TextComponent;
import one.oth3r.otterlib.base.LoaderUtilities;
import one.oth3r.otterlib.base.OtterLogger;
import one.oth3r.otterlib.chat.CTxT;

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
     * @return the CTxT
     */
    @Override
    public CTxT getCTxTFromObj(Object obj) {
        CTxT output = new CTxT();
        // append the correctly cast object to the output
        if (obj instanceof CTxT) output.append(((CTxT) obj).b());
        else if (obj instanceof TextComponent) output.append((TextComponent) obj);
        // else, try to convert into a string
        else output.append(String.valueOf(obj));

        return output;
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
}
