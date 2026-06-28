package one.oth3r.otterlib.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import one.oth3r.otterlib.chat.hover.HoverTxT;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Generic loader text for spigot
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
        super(text.duplicate());
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
        this.text = new TextComponent(text);
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T text(TextComponent text) {
        this.text = text.duplicate();
        return (T) this;
    }

    @Override @SuppressWarnings("unchecked")
    public T text(T text) {
        this.text = text.text.duplicate();
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

    private ClickEvent getClickEvent() {
        if (this.clickEvent == null) return null;
        return switch (this.clickEvent.getAction()) {
            case NOTHING -> null;
            case RUN_COMMAND -> new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickEvent.getActionString());
            case SUGGEST_COMMAND -> new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, clickEvent.getActionString());
            case OPEN_URL -> {
                try {
                    yield new ClickEvent(ClickEvent.Action.OPEN_URL, new URI(clickEvent.getActionString()).toASCIIString());
                } catch (URISyntaxException e) {
                    yield null;
                }
            }
            case OPEN_FILE -> new ClickEvent(ClickEvent.Action.OPEN_FILE, clickEvent.getActionString());
            case COPY_TO_CLIPBOARD -> new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, clickEvent.getActionString());
            case CHANGE_PAGE -> new ClickEvent(ClickEvent.Action.CHANGE_PAGE, clickEvent.getActionString());
        };
    }

    @SuppressWarnings("unchecked")
    private HoverEvent getHoverEvent() {
        if (this.hoverEvent == null) return null;
        if (this.hoverEvent instanceof HoverTxT<?>) {
            try {
                return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(((LoaderText<T>) hoverEvent.getActionObject()).b()).create()));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * applies styles to the provided text component
     * @param tc the text component to apply styles to
     * @param wrapper if the text component provided is part of the wrapper or not (skip extra styles)
     */
    private void applyStyle(TextComponent tc, boolean wrapper) {
        tc.setClickEvent(getClickEvent());
        tc.setHoverEvent(getHoverEvent());
        if (wrapper) return;
        if (this.color != null) tc.setColor(ChatColor.of(this.color));
        tc.setItalic(this.italic);
        tc.setBold(this.bold);
        tc.setStrikethrough(this.strikethrough);
        tc.setUnderlined(this.underline);
    }

    @Override @SuppressWarnings("unchecked")
    public TextComponent b() {
        TextComponent output = new TextComponent();
        
        if (this.wrapper != null) {
            TextComponent frontBracket = this.wrapper.front().b();
            applyStyle(frontBracket, true);
            output.addExtra(frontBracket);
        }

        if (this.rainbow != null && this.rainbow.isEnabled()) {
            this.rainbow.colorize(text.toPlainText(), this::createText).forEach(textComponent -> {
                TextComponent tc = textComponent.b();
                applyStyle(tc,false);
                output.addExtra(tc);
            });
        } else {
            TextComponent tc = this.text.duplicate();
            applyStyle(tc, false);
            output.addExtra(tc);
        }

        if (this.wrapper != null) {
            TextComponent backBracket = wrapper.back().b();
            applyStyle(backBracket, true);
            output.addExtra(wrapper.back().b());
        }

        for (LoaderText<T> txt : this.append) {
            T self = (T) this;
            txt.copyIfChanged(self);
            output.addExtra(txt.b());
        }

        return output;
    }

    @Override
    public String toString() {
        return b().toPlainText();
    }
}
