package one.oth3r.otterlib.chat;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import one.oth3r.otterlib.base.Num;

import java.net.URI;

public class CTxT extends ChatText<MutableText, CTxT> {
    public CTxT() {
        super();
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

    @Override
    public CTxT clone() {
        return new CTxT(this);
    }

    @Override
    public CTxT text(String text) {
        this.text = Text.literal(text);
        return this;
    }

    /**
     * sets the text of this ChatText to the text of the provided ChatText
     *
     * @param text
     */
    @Override
    public CTxT text(MutableText text) {
        this.text = text.copy();
        return this;
    }

    @Override
    public CTxT text(CTxT text) {
        this.text = text.text;
        return this;
    }

    @Override
    public CTxT append(String append) {
        this.append.add(new CTxT(append));
        return this;
    }

    @Override
    public CTxT append(MutableText append) {
        this.append.add(new CTxT(append));
        return this;
    }

    private ClickEvent getClickEvent() {
        if (this.clickEvent == null) return null;
        return switch (this.clickEvent.getAction()) {
            case RUN_COMMAND -> new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEvent.getActionString());
            case SUGGEST_COMMAND -> new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickEvent.getActionString());
            case OPEN_URL -> new ClickEvent(ClickEvent.Action.OPEN_URL, clickEvent.getActionString());
            case OPEN_FILE -> new ClickEvent(ClickEvent.Action.OPEN_FILE, clickEvent.getActionString());
            case COPY_TO_CLIPBOARD -> new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickEvent.getActionString());
            case CHANGE_PAGE -> new ClickEvent(ClickEvent.Action.CHANGE_PAGE, clickEvent.getActionString());
        };
    }

    private HoverEvent getHoverEvent() {
        if (this.hoverEvent == null) return null;
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, this.hoverEvent.b());
    }

    @Override
    public MutableText b() {
        MutableText output = Text.literal("");
        if (this.rainbow.isEnabled()) {
            this.text = this.rainbow.colorize(text.getString(), this).b();
        }

        if (this.button) output.append("[").setStyle(Style.EMPTY.withColor(Formatting.byCode('f')));

        output.append(this.text.styled(style -> style
                .withColor(TextColor.fromRgb(this.color.getRGB()))
                .withClickEvent(getClickEvent())
                .withHoverEvent(getHoverEvent())
                .withItalic(this.italic)
                .withBold(this.bold)
                .withStrikethrough(this.strikethrough)
                .withUnderline(this.underline)
                .withObfuscated(this.obfuscate)));
        if (this.button) output.append("]").setStyle(Style.EMPTY.withColor(Formatting.byCode('f')));

        // make sure everything including the button pieces are styled ?
        output.styled(style -> style
                .withClickEvent(getClickEvent())
                .withHoverEvent(getHoverEvent())
                .withItalic(this.italic)
                .withBold(this.bold)
                .withStrikethrough(this.strikethrough)
                .withUnderline(this.underline)
                .withObfuscated(this.obfuscate));

        for (CTxT txt : this.append) output.append(txt.b());

        return output.copy();
    }

    @Override
    public String toString() {
        return b().getString();
    }
}
