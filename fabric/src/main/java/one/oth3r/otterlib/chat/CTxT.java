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

    @Override
    protected CTxT createText(String text) {
        return new CTxT(text);
    }

    @Override
    protected CTxT createText(MutableComponent text) {
        return new CTxT(text);
    }

    @Override
    protected CTxT createCopy(CTxT text) {
        return new CTxT(text);
    }

    @Override
    public CTxT clone() {
        return new CTxT(this);
    }
}
