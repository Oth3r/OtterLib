package one.oth3r.otterlib.chat;

import net.kyori.adventure.text.TextComponent;

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
    public CTxT clone() {
        return new CTxT(this);
    }
}