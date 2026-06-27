package one.oth3r.otterlib.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import one.oth3r.otterlib.chat.hover.HoverAction;
import one.oth3r.otterlib.chat.hover.HoverTxT;

/**
 * Generic loader text for paper
 * @param <T> the type of the text
 */
public class LoaderText<T extends LoaderText<T>> extends ChatText<TextComponent, T> {
    public LoaderText() {
        super();
    }

    public LoaderText(T main) {
        super(main);
    }

    public LoaderText(String text) {
        super(text);
    }

    public LoaderText(TextComponent text) {
        super(text.toBuilder().build());
    }

    @Override @SuppressWarnings("unchecked")
    public T clone() {
        return (T) new LoaderText<>((T) this);
    }

    @Override @SuppressWarnings("unchecked")
    public T text(String text) {
        this.text = Component.text(text);
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T text(TextComponent text) {
        this.text = text.toBuilder().build();
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T text(T text) {
        this.text = text.text.toBuilder().build();
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T append(String append) {
        this.append.add((T) new LoaderText<>(append));
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T append(TextComponent append) {
        this.append.add((T) new LoaderText<>(append));
        return (T) this;
    }

    private ClickEvent getClickEvent() {
        if (this.clickEvent == null) return null;
        return switch (this.clickEvent.getAction()) {
            case NOTHING -> null;
            case RUN_COMMAND -> ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, clickEvent.getActionString());
            case SUGGEST_COMMAND -> ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickEvent.getActionString());
            case OPEN_URL -> ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, clickEvent.getActionString());
            case OPEN_FILE -> ClickEvent.clickEvent(ClickEvent.Action.OPEN_FILE, clickEvent.getActionString());
            case COPY_TO_CLIPBOARD -> ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickEvent.getActionString());
            case CHANGE_PAGE -> ClickEvent.clickEvent(ClickEvent.Action.CHANGE_PAGE, clickEvent.getActionString());
        };
    }

    @SuppressWarnings("unchecked")
    private HoverEvent<Component> getHoverEvent() {
        if (this.hoverEvent == null) return null;
        if (this.hoverEvent instanceof HoverTxT<?>) {
            return HoverEvent.showText(((LoaderText<T>) hoverEvent.getActionObject()).b());
        }
        return null;
    }

    private void applyStyle(TextComponent.Builder builder) {
        if (this.color != null) builder.color(TextColor.color(this.color.getRGB()));
        builder.clickEvent(getClickEvent());
        builder.hoverEvent(getHoverEvent());
        if (this.italic) builder.decorate(TextDecoration.ITALIC);
        if (this.bold) builder.decorate(TextDecoration.BOLD);
        if (this.strikethrough) builder.decorate(TextDecoration.STRIKETHROUGH);
        if (this.underline) builder.decorate(TextDecoration.UNDERLINED);
    }

    @Override @SuppressWarnings("unchecked")
    public TextComponent b() {
        TextComponent.Builder builder = Component.text();
        
        if (this.wrapper != null) {
            TextComponent.Builder front = Component.text("[").toBuilder();
            applyStyle(front);
            builder.append(front.build());
        }

        if (this.rainbow != null && this.rainbow.isEnabled()) {
            this.rainbow.colorize(PlainTextComponentSerializer.plainText().serialize(text)).forEach(textComponent -> {
                TextComponent tc = textComponent.b();
                TextComponent.Builder b = tc.toBuilder();
                applyStyle(b);
                builder.append(b.build());
            });
        } else {
            TextComponent.Builder b = this.text.toBuilder();
            applyStyle(b);
            builder.append(b.build());
        }

        if (this.wrapper != null) {
            TextComponent.Builder back = Component.text("]").toBuilder();
            applyStyle(back);
            builder.append(back.build());
        }

        for (LoaderText<T> txt : this.append) {
            T self = (T) this;
            txt.copyIfChanged(self);
            builder.append(txt.b());
        }

        return builder.build();
    }

    @Override
    public String toString() {
        return PlainTextComponentSerializer.plainText().serialize(b());
    }
}