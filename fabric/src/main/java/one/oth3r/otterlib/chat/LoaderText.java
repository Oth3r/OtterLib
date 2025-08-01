package one.oth3r.otterlib.chat;

import net.minecraft.text.*;
import one.oth3r.otterlib.base.Num;
import one.oth3r.otterlib.chat.hover.HoverTxT;

import java.awt.*;
import java.net.URI;
import java.util.function.UnaryOperator;

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

    @Override @SuppressWarnings("unchecked")
    public MutableText b() {
        MutableText output = Text.literal("");
        UnaryOperator<Style> styleUpdater = style -> style
                .withColor(TextColor.fromRgb((this.color == null ? Color.WHITE : this.color).getRGB()))
                .withClickEvent(getClickEvent())
                .withHoverEvent(getHoverEvent())
                .withItalic(this.italic)
                .withBold(this.bold)
                .withStrikethrough(this.strikethrough)
                .withUnderline(this.underline)
                .withObfuscated(this.obfuscate);

        if (this.wrapper != null) {
            output.append(wrapper.front().hover(this.hoverEvent).click(this.clickEvent).b());
        }

        if (this.rainbow != null && this.rainbow.isEnabled()) {
            this.rainbow.colorize(text.getString()).forEach(text -> output.append(text.b().styled(styleUpdater)));
        } else {
            output.append(this.text.styled(styleUpdater));
        }


        for (LoaderText<T> txt : this.append) {
            txt.copyIfChanged((T) this);
            output.append(txt.b());
        }

        if (this.wrapper != null) {
            output.append(wrapper.back().hover(this.hoverEvent).click(this.clickEvent).b());
        }

        return output.copy();
    }

    @Override
    public String toString() {
        return b().getString();
    }
}
