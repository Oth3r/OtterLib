package one.oth3r.otterlib.chat;

import net.minecraft.text.MutableText;

public class CTxT extends LoaderText<CTxT> {
    public CTxT() {
    }

    public CTxT(CTxT main) {
        super(main);
    }

    public CTxT(String text) {
        super(text);
    }

    public CTxT(MutableText text) {
        super(text);
    }
}
