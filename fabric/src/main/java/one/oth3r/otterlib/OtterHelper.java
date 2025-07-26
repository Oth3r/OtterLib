package one.oth3r.otterlib;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import one.oth3r.otterlib.base.LoaderUtilities;
import one.oth3r.otterlib.base.OtterLogger;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.LoaderText;

public class OtterHelper implements LoaderUtilities {
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
    @Override @SuppressWarnings("unchecked")
    public <T extends LoaderText<T>> T getTxTFromObj(Object obj) {
        if (obj instanceof LoaderText<?>) return (T) new LoaderText<>(((T) obj).b());
        else if (obj instanceof Text) return (T) new LoaderText<>((MutableText) obj);
        // else, try to convert into a string
        else return (T) new LoaderText<>(String.valueOf(obj));
    }

    /**
     * a method to check if OtterLib is running on the client or not
     */
    @Override
    public boolean isClient() {
        return OtterLib.isClient();
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    /**
     * only works on client* <br/>
     * gets the localized text using the client side language system
     */
    @Override
    public CTxT getClientTranslatable(String key, Object... args) {
        return new CTxT(Text.translatable(key, args));
    }
}
