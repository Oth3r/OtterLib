package one.oth3r.otterlib;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import one.oth3r.otterlib.base.LoaderUtilities;
import one.oth3r.otterlib.base.OtterLogger;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.chat.LoaderText;
import one.oth3r.otterlib.chat.Wrapper;

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
    @Override
    public CTxT getCTxTFromObj(Object obj) {
        return switch (obj) {
            case CTxT txt -> txt.clone();
            case LoaderText<?> txt -> new CTxT(txt.b());
            case Component _ -> new CTxT((MutableComponent) obj);
            // else, try to convert into a string
            case null, default -> new CTxT(String.valueOf(obj));
        };
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
        return new CTxT(Component.translatable(key, args));
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
