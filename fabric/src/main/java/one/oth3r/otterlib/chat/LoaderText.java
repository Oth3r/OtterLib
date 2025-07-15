package one.oth3r.otterlib.chat;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import one.oth3r.otterlib.base.Num;
import one.oth3r.otterlib.chat.hover.HoverTxT;

import java.net.URI;

public class LoaderText<T extends LoaderText<T>> extends ChatText<MutableText, T> {
    public LoaderText() {
        super();
    }

    public LoaderText(T main) {
        super(main);
    }

    public LoaderText(String text) {
        super(text);
    }

    public LoaderText(MutableText text) {
        super(text);
    }

    @Override @SuppressWarnings("unchecked")
    public T clone() {
        return (T) new LoaderText<>((T) this);
    }

    @Override @SuppressWarnings("unchecked")
    public T text(String text) {
        this.text = Text.literal(text);
        return (T) this;
    }

    /**
     * sets the text of this ChatText to the text of the provided ChatText
     *
     * @param text
     */
    @Override @SuppressWarnings("unchecked")
    public T text(MutableText text) {
        this.text = text.copy();
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T text(T text) {
        this.text = text.text.copy();
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T append(String append) {
        this.append.add((T) new LoaderText<>(append));
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T append(MutableText append) {
        this.append.add((T) new LoaderText<>(append));
        return (T) this;
    }

    private ClickEvent getClickEvent() {
        if (this.clickEvent == null) return null;
        return switch (this.clickEvent.getAction()) {
            case NOTHING -> null;
            case RUN_COMMAND -> new ClickEvent.RunCommand(clickEvent.getActionString());
            case SUGGEST_COMMAND -> new ClickEvent.SuggestCommand(clickEvent.getActionString());
            case OPEN_URL -> new ClickEvent.OpenUrl(URI.create(clickEvent.getActionString()));
            case OPEN_FILE -> new ClickEvent.OpenFile(clickEvent.getActionString());
            case COPY_TO_CLIPBOARD -> new ClickEvent.CopyToClipboard(clickEvent.getActionString());
            case CHANGE_PAGE -> new ClickEvent.ChangePage(Num.toInt(clickEvent.getActionString()));
        };
    }

    @SuppressWarnings("unchecked")
    private HoverEvent getHoverEvent() {
        if (this.hoverEvent == null) return null;
        if (this.hoverEvent instanceof HoverTxT<?>) {
            return new HoverEvent.ShowText(((LoaderText<T>) hoverEvent.getActionObject()).b());
        }
        // not an official hover event, null
        return null;
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

        for (LoaderText<T> txt : this.append) output.append(txt.b());

        return output.copy();
    }

    @Override
    public String toString() {
        return b().getString();
    }
}
