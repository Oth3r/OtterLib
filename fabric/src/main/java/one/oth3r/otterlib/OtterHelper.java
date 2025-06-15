package one.oth3r.otterlib;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import one.oth3r.otterlib.base.LoaderUtilities;
import one.oth3r.otterlib.chat.CTxT;

import java.util.logging.Logger;

public class OtterHelper implements LoaderUtilities {
    @Override
    public Logger getLogger() {
        return Logger.getLogger("OtterLib");
    }

    /**
     * Trys to convert the object into a CTxT. <br/>
     * on different loaders, different tactics are used to grab the loader's different default Text implementations
     *
     * @param obj
     * @return the CTxT
     */
    @Override
    public CTxT getCTxTFromObj(Object obj) {
        CTxT output = new CTxT();
        // append the correctly cast object to the output
        if (obj instanceof CTxT) output.append(((CTxT) obj).b());
        else if (obj instanceof Text) output.append((MutableText) obj);
        // else, try to convert into a string
        else output.append(String.valueOf(obj));

        return output;
    }
}
