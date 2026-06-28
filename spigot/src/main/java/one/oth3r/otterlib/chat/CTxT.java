package one.oth3r.otterlib.chat;

import net.md_5.bungee.api.chat.TextComponent;

public class CTxT extends LoaderText<CTxT> {
    public CTxT() {
        super();
    }

    public CTxT(CTxT main) {
        super(main);
    }

    public CTxT(String text) {
        super(text);
    }

    public CTxT(TextComponent text) {
        super(text);
    }

    @Override
    protected CTxT createText(String text) {
        return new CTxT(text);
    }

    @Override
    protected CTxT createText(TextComponent text) {
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
