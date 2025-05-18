package one.oth3r.otterlib.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.client.screen.utl.CustomImage;
import one.oth3r.otterlib.client.screen.utl.SimpleButton;
import one.oth3r.otterlib.client.screen.widget.ClickableImageWidget;
import one.oth3r.otterlib.client.screen.widget.ConfigsListWidget;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ConfigScreen extends Screen implements SetClientScreen {
    private static final int HEADER_HEIGHT = 30, FOOTER_HEIGHT = 40;
    protected final Screen parent;

    public final ThreePartsLayoutWidget layout;
    protected final ClickableImageWidget customBanner;
    protected ConfigsListWidget body;
    protected List<SimpleButton> fileButtons;
    protected List<SimpleButton> footer;

    /**
     * creates a config screen with a custom title image via {@link ClickableImageWidget}
     * @param parent the parent screen
     * @param title the title of the config screen
     * @param customBanner a custom banner image at the top of the screen
     * @param fileButtons a list of buttons - should be 30px tall.
     * @param footer a list of buttons to show in the footer of the screen - should include one {@link SimpleButton.Templates#done(CTxT)} / {@link SimpleButton.Close} button to close the screen
     */
    public ConfigScreen(Screen parent, @NotNull CTxT title, @NotNull ClickableImageWidget customBanner, @NotNull List<SimpleButton> fileButtons, List<SimpleButton> footer) {
        super(title.b());
        this.parent = parent;
        this.customBanner = customBanner;
        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT+customBanner.getHeight(),FOOTER_HEIGHT);
        this.fileButtons = fileButtons;
        this.footer = footer;
    }

    /**
     * creates a config screen with a custom title image via {@link CustomImage}
     * @param parent the parent screen
     * @param title the title of the config screen
     * @param customBanner a custom banner image at the top of the screen
     * @param fileButtons a list of buttons - should be 30px tall.
     * @param footer a list of buttons to show in the footer of the screen - should include one {@link SimpleButton.Templates#done(CTxT)} / {@link SimpleButton.Close} button to close the screen
     */
    public ConfigScreen(Screen parent, @NotNull CTxT title, @NotNull CustomImage customBanner, @NotNull List<SimpleButton> fileButtons, List<SimpleButton> footer) {
        super(title.b());
        this.parent = parent;
        this.customBanner = new ClickableImageWidget.Builder(new CTxT(Text.translatable("otterlib.gui.screen.title_image")),
                this.textRenderer, customBanner).build();
        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT+ this.customBanner.getHeight(),FOOTER_HEIGHT);
        this.fileButtons = fileButtons;
        this.footer = footer;
    }

    /**
     * creates a config screen
     * @param parent the parent screen
     * @param title the title of the config screen
     * @param fileButtons a list of buttons - should be 30px tall.
     * @param footer a list of buttons to show in the footer of the screen - should include one {@link SimpleButton.Templates#done(CTxT)} / {@link SimpleButton.Close} button to close the screen
     */
    public ConfigScreen(Screen parent, @NotNull CTxT title, @NotNull List<SimpleButton> fileButtons, List<SimpleButton> footer) {
        super(title.b());
        this.parent = parent;
        this.customBanner = null;
        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT,FOOTER_HEIGHT);
        this.fileButtons = fileButtons;
        this.footer = footer;
    }

    public void setScreen(Screen screen) {
        this.client.setScreen(screen);
    }

    @Override
    protected void init() {
        this.initHeader();

        this.layout.addBody(new GridWidget().setColumnSpacing(10));
        this.body = this.addSelectableChild(new ConfigsListWidget(client, this));
        body.addAllConfigEntries(this.fileButtons.stream().map(b -> ConfigsListWidget.ConfigEntry.create(b.build(this),this.body)).collect(Collectors.toList()));

        // FOOTER

        GridWidget footerWidget = this.layout.addFooter(new GridWidget().setColumnSpacing(10));
        AtomicInteger i = new AtomicInteger();
        this.footer.forEach(f -> footerWidget.add(f.build(this),0, i.getAndIncrement()));


        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    private void initHeader() {
        this.layout.addHeader(Objects.requireNonNullElseGet(customBanner, () -> new TextWidget(this.title, this.textRenderer)));
    }

    public ConfigsListWidget getBody() {
        return body;
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.body.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
