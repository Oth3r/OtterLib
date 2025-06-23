package one.oth3r.otterlib.chat;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class CTxT extends ChatText<TextComponent, CTxT> {
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

    @Override
    public CTxT text(String text) {
        this.text = Component.text(text);
        return this;
    }

    @Override
    public CTxT text(TextComponent text) {
        this.text = text.toBuilder().build();
        return this;
    }

    @Override
    public CTxT text(CTxT text) {
        this.text = text.text.toBuilder().build();
        return this;
    }

    @Override
    public CTxT append(String append) {
        this.append.add(new CTxT(append));
        return this;
    }

    @Override
    public CTxT append(TextComponent append) {
        this.append.add(new CTxT(append));
        return this;
    }

    private ClickEvent getClickEvent() {
        if (this.clickEvent == null) return null;
        return switch (this.clickEvent.getAction()) {
            case RUN_COMMAND -> ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,clickEvent.getActionString());
            case SUGGEST_COMMAND -> ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,clickEvent.getActionString());
            case OPEN_URL -> ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL,clickEvent.getActionString());
            case OPEN_FILE -> ClickEvent.clickEvent(ClickEvent.Action.OPEN_FILE,clickEvent.getActionString());
            case COPY_TO_CLIPBOARD -> ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,clickEvent.getActionString());
            case CHANGE_PAGE -> ClickEvent.clickEvent(ClickEvent.Action.CHANGE_PAGE,clickEvent.getActionString());
        };
    }

    private HoverEvent<Component> getHoverEvent() {
        if (this.hoverEvent == null) return null;
        return HoverEvent.showText(this.hoverEvent.b());
    }

    @Override
    public TextComponent b() {
        TextComponent.Builder output = Component.text();
        if (this.rainbow.isEnabled()) {
            text = this.rainbow.colorize(PlainTextComponentSerializer.plainText().serialize(text), this).b();
        } else text = text.toBuilder().color(TextColor.color(this.color.getRGB())).build();
        TextComponent.Builder textBuilder = text.toBuilder();
        format(textBuilder);
        this.text = textBuilder.build();

        if (this.button) output.append(Component.text("["));
        output.append(text);
        if (this.button) output.append(Component.text("]"));
        format(output);
        for (CTxT txt : this.append) output.append(txt.b());
        return output.build();
    }

    private void format(TextComponent.Builder target) {
        target.clickEvent(getClickEvent());
        target.hoverEvent(getHoverEvent());
        if (this.italic) target.decorate(TextDecoration.ITALIC);
        if (this.bold) target.decorate(TextDecoration.BOLD);
        if (this.strikethrough) target.decorate(TextDecoration.STRIKETHROUGH);
        if (this.underline) target.decorate(TextDecoration.UNDERLINED);
    }

    @Override
    public String toString() {
        return b().content();
    }
}
