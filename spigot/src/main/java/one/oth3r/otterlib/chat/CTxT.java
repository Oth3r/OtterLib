package one.oth3r.otterlib.chat;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

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
        this.text = new TextComponent(text);
        return this;
    }

    @Override
    public CTxT text(TextComponent text) {
        this.text = text.duplicate();
        return this;
    }

    @Override
    public CTxT text(CTxT text) {
        this.text = text.text.duplicate();
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
            case RUN_COMMAND -> new ClickEvent(ClickEvent.Action.RUN_COMMAND,clickEvent.getActionString());
            case SUGGEST_COMMAND -> new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,clickEvent.getActionString());
            case OPEN_URL -> new ClickEvent(ClickEvent.Action.OPEN_URL,clickEvent.getActionString());
            case OPEN_FILE -> new ClickEvent(ClickEvent.Action.OPEN_FILE,clickEvent.getActionString());
            case COPY_TO_CLIPBOARD -> new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,clickEvent.getActionString());
            case CHANGE_PAGE -> new ClickEvent(ClickEvent.Action.CHANGE_PAGE,clickEvent.getActionString());
        };
    }

    private HoverEvent getHoverEvent() {
        if (this.hoverEvent == null) return null;
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(this.hoverEvent.b()).create()));
    }

    @Override
    public TextComponent b() {
        TextComponent output = new TextComponent();
        if (this.rainbow.isEnabled()) {
            text = this.rainbow.colorize(text.toPlainText(), this).b();
        } else text.setColor(ChatColor.of(this.color));
        text.setClickEvent(getClickEvent());
        text.setHoverEvent(getHoverEvent());
        text.setItalic(this.italic);
        text.setBold(this.bold);
        text.setStrikethrough(this.strikethrough);
        text.setUnderlined(this.underline);

        if (this.button) output.addExtra("[");
        output.addExtra(text);
        if (this.button) output.addExtra("]");
        output.setClickEvent(getClickEvent());
        output.setHoverEvent(getHoverEvent());
        output.setItalic(this.italic);
        output.setBold(this.bold);
        output.setStrikethrough(this.strikethrough);
        output.setUnderlined(this.underline);
        for (CTxT txt : this.append) output.addExtra(txt.b());
        return output;
    }

    @Override
    public String toString() {
        return b().toPlainText();
    }
}
