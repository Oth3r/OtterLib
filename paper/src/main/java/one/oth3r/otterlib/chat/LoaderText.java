package one.oth3r.otterlib.chat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import one.oth3r.otterlib.base.Num;
import one.oth3r.otterlib.chat.hover.HoverTxT;

/**
 * Generic loader text for paper
 * @param <T> the type of the text
 */
public abstract class LoaderText<T extends LoaderText<T>> extends ChatText<TextComponent, T> {
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

    protected abstract T createText(String text);

    protected abstract T createText(TextComponent text);

    protected abstract T createCopy(T text);

    @Override
    public T clone() {
        return createCopy(self());
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

    @Override
    public T append(String append) {
        this.append.add(createText(append));
        return self();
    }

    @Override
    public T append(TextComponent append) {
        this.append.add(createText(append));
        return self();
    }

    private ClickEvent<?> getClickEvent() {
        if (this.clickEvent == null) return null;
        return switch (this.clickEvent.getAction()) {
            case NOTHING -> null;
            case RUN_COMMAND -> ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,ClickEvent.Payload.string(clickEvent.getActionString()));
            case SUGGEST_COMMAND -> ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, ClickEvent.Payload.string(clickEvent.getActionString()));
            case OPEN_URL -> ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, ClickEvent.Payload.string(clickEvent.getActionString()));
            case OPEN_FILE -> ClickEvent.clickEvent(ClickEvent.Action.OPEN_FILE, ClickEvent.Payload.string(clickEvent.getActionString()));
            case COPY_TO_CLIPBOARD -> ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ClickEvent.Payload.string(clickEvent.getActionString()));
            case CHANGE_PAGE -> ClickEvent.clickEvent(ClickEvent.Action.CHANGE_PAGE, ClickEvent.Payload.integer(Num.toInt(clickEvent.getActionString())));
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

    /**
     * applies styles to the provided builder
     * @param builder the builder to apply styles to
     * @param wrapper if the builder provided is part of the wrapper or not (skip extra styles)
     */
    private void applyStyle(TextComponent.Builder builder, boolean wrapper) {
        builder.clickEvent(getClickEvent());
        builder.hoverEvent(getHoverEvent());
        if (wrapper) return;
        if (this.color != null) builder.color(TextColor.color(this.color.getRGB()));
        if (this.italic) builder.decorate(TextDecoration.ITALIC);
        if (this.bold) builder.decorate(TextDecoration.BOLD);
        if (this.strikethrough) builder.decorate(TextDecoration.STRIKETHROUGH);
        if (this.underline) builder.decorate(TextDecoration.UNDERLINED);
    }

    @Override @SuppressWarnings("unchecked")
    public TextComponent b() {
        TextComponent.Builder builder = Component.text();
        
        if (this.wrapper != null) {
            TextComponent.Builder front = this.wrapper.front().b().toBuilder();
            applyStyle(front, true);
            builder.append(front.build());
        }

        if (this.rainbow != null && this.rainbow.isEnabled()) {
            this.rainbow.colorize(PlainTextComponentSerializer.plainText().serialize(text), this::createText).forEach(textComponent -> {
                TextComponent tc = textComponent.b();
                TextComponent.Builder b = tc.toBuilder();
                applyStyle(b, false);
                builder.append(b.build());
            });
        } else {
            TextComponent.Builder b = this.text.toBuilder();
            applyStyle(b, false);
            builder.append(b.build());
        }

        if (this.wrapper != null) {
            TextComponent.Builder back = this.wrapper.back().b().toBuilder();
            applyStyle(back, true);
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
