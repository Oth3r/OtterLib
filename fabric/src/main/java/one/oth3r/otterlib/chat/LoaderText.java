package one.oth3r.otterlib.chat;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import one.oth3r.otterlib.base.Num;
import one.oth3r.otterlib.chat.hover.HoverTxT;

import java.awt.*;
import java.net.URI;
import java.util.function.UnaryOperator;

public abstract class LoaderText<T extends LoaderText<T>> extends ChatText<MutableComponent, T> {
    public LoaderText() {
        super();
    }

    public LoaderText(T main) {
        super(main);
    }

    public LoaderText(String text) {
        super(text);
    }

    public LoaderText(MutableComponent text) {
        super(text);
    }

    protected abstract T createText(String text);

    protected abstract T createText(MutableComponent text);

    protected abstract T createCopy(T text);

    @Override
    public T clone() {
        return createCopy(self());
    }

    @Override @SuppressWarnings("unchecked")
    public T text(String text) {
        this.text = Component.literal(text);
        return (T) this;
    }

    /**
     * sets the text of this ChatText to the text of the provided ChatText
     *
     * @param text
     */
    @Override @SuppressWarnings("unchecked")
    public T text(MutableComponent text) {
        this.text = text.copy();
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T text(T text) {
        this.text = text.text.copy();
        return (T) this;
    }

    @Override
    public T append(String append) {
        this.append.add(createText(append));
        return self();
    }

    @Override
    public T append(MutableComponent append) {
        this.append.add(createText(append));
        return self();
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
    public MutableComponent b() {
        MutableComponent output = Component.literal("");
        UnaryOperator<Style> styleUpdater = style -> style
                .withColor(TextColor.fromRgb((this.color == null ? Color.WHITE : this.color).getRGB()))
                .withClickEvent(getClickEvent())
                .withHoverEvent(getHoverEvent())
                .withItalic(this.italic)
                .withBold(this.bold)
                .withStrikethrough(this.strikethrough)
                .withUnderlined(this.underline)
                .withObfuscated(this.obfuscate);

        if (this.wrapper != null) {
            output.append(wrapper.front().hover(this.hoverEvent).click(this.clickEvent).b());
        }

        if (this.rainbow != null && this.rainbow.isEnabled()) {
            this.rainbow.colorize(text.getString(), this::createText).forEach(text -> output.append(text.b().withStyle(styleUpdater)));
        } else {
            output.append(this.text.withStyle(styleUpdater));
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
