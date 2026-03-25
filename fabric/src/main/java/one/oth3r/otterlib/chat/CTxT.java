package one.oth3r.otterlib.chat;

import net.minecraft.network.chat.MutableComponent;

public class CTxT extends LoaderText<CTxT> {
    public CTxT() {
    }

    public CTxT(CTxT main) {
        super(main);
    }

    public CTxT(String text) {
        super(text);
    }

    public CTxT(MutableComponent text) {
        super(text.copy());
    }
}
