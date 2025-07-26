package one.oth3r.otterlib.chat;

import one.oth3r.otterlib.chat.click.ClickAction;
import one.oth3r.otterlib.chat.hover.HoverAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * helper class for minecraft chat messages (common)
 * @param <T> the text object for the loader being used
 * @param <C> the main object that is used by the modloader
 */
public abstract class ChatText<T, C extends ChatText<T, C>> {
    protected T text;
    protected Boolean button = false;
    protected Color color = null;
    protected ClickAction clickEvent = null;
    protected HoverAction<?> hoverEvent = null;
    protected Boolean bold = false;
    protected Boolean italic = false;
    protected Boolean strikethrough = false;
    protected Boolean underline = false;
    protected Boolean obfuscate = false;
    protected ArrayList<C> append = new ArrayList<>();
    protected Rainbow rainbow = new Rainbow();

    public ChatText() {
        text("");
    }

    public ChatText(C main) {
        copyFromObject(main);
    }

    public ChatText(String text) {
        text(text);
    }

    public ChatText(T text) {
        text(text);
    }

    public void copyFromObject(C old) {
        this.text = old.text;
        this.button = old.button;
        this.color = old.color;
        this.clickEvent = old.clickEvent;
        this.hoverEvent = old.hoverEvent;
        this.bold = old.bold;
        this.italic = old.italic;
        this.strikethrough = old.strikethrough;
        this.underline = old.underline;
        this.append = old.append.stream().map(ChatText::clone).collect(Collectors.toCollection(ArrayList::new));;
        this.rainbow = old.rainbow;
    }

    /**
     * mirrors the properties from source if the properties haven't been changed in this object.
     * properties that are skipped are: append, button & text.
     */
    public void copyIfChanged(C source) {
        if (this.color == null) this.color = source.color;
        if (this.clickEvent == null) this.clickEvent = source.clickEvent;
        if (this.hoverEvent == null) this.hoverEvent = source.hoverEvent;
        if (!this.bold) this.bold = source.bold;
        if (!this.italic) this.italic = source.italic;
        if (!this.strikethrough) this.strikethrough = source.strikethrough;
        if (!this.underline) this.underline = source.underline;
        if (!this.obfuscate) this.obfuscate = source.obfuscate;
        if (!this.rainbow.equals(new Rainbow())) this.rainbow = source.rainbow;
    }

    @Override
    public abstract C clone();

    public abstract C text(String text);

    /**
     * sets the text of this ChatText to the text of the provided ChatText
     */
    public abstract C text(T text);

    public abstract C text(C text);

    public C btn(Boolean button) {
        this.button = button;
        return self();
    }

    public C color(Color color) {
        this.color = color;
        return self();
    }

    public C color(String color) {
        this.color = Color.decode(color);
        return self();
    }

    public C click(ClickAction clickAction) {
        this.clickEvent = clickAction;
        return self();
    }

    public C hover(HoverAction<?> hoverAction) {
        this.hoverEvent = hoverAction;
        return self();
    }

    public C bold(Boolean bold) {
        this.bold = bold;
        return self();
    }

    public C bold() {
        this.bold = true;
        return self();
    }

    public C italic(Boolean italic) {
        this.italic = italic;
        return self();
    }

    public C italic() {
        this.italic = true;
        return self();
    }

    public C strikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return self();
    }

    public C strikethrough() {
        this.strikethrough = true;
        return self();
    }

    public C underline(Boolean underline) {
        this.underline = underline;
        return self();
    }

    public C underline() {
        this.underline = true;
        return self();
    }

    public C obfuscate(Boolean obfuscate) {
        this.obfuscate = obfuscate;
        return self();
    }

    public C obfuscate() {
        this.obfuscate = true;
        return self();
    }

    public abstract C append(String append);

    public abstract C append(T append);

    public C append(C append) {
        this.append.add(append.clone());
        return self();
    }

    public C rainbow(Rainbow rainbow) {
        this.rainbow = rainbow;
        return self();
    }

    public abstract T b();

    @Override
    public abstract String toString();

    public T getText() {
        return text;
    }

    public Boolean isBtn() {
        return button;
    }

    public Color getColor() {
        return color;
    }

    public Boolean isBold() {
        return bold;
    }

    public Boolean isItalic() {
        return italic;
    }

    public Boolean isStrikethrough() {
        return strikethrough;
    }

    public Boolean isUnderline() {
        return underline;
    }

    public Boolean isObfuscated() {
        return obfuscate;
    }

    public ClickAction getClick() {
        return clickEvent;
    }

    public HoverAction<?> getHover() {
        return hoverEvent;
    }

    public ArrayList<C> getAppends() {
        return new ArrayList<>(append);
    }

    public boolean isEmpty() {
        return this.toString().isEmpty();
    }

    // helper to return the subclass instance
    @SuppressWarnings("unchecked")
    protected C self() {
        return (C) this;
    }
}
