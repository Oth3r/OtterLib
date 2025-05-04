package one.oth3r.otterlib.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.text.Text;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.client.screen.utl.CustomImage;
import one.oth3r.otterlib.client.screen.utl.SimpleButton;
import one.oth3r.otterlib.client.screen.widget.ClickableImageWidget;
import one.oth3r.otterlib.client.screen.widget.ConfigsListWidget;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigScreen extends Screen implements SetClientScreen {
    private static final int HEADER_HEIGHT = 30, FOOTER_HEIGHT = 40;
    protected final Screen parent;

    public final ThreePartsLayoutWidget layout;
    protected final CustomImage customImage;
    protected ConfigsListWidget body;
    protected List<SimpleButton> fileButtons;
    protected List<SimpleButton> footer;

    /**
     * creates a config screen with a custom title image
     * @param parent the parent screen
     * @param title the title of the config screen
     * @param customImage a custom banner at the top of the screen
     * @param fileButtons a list of buttons - should be 30px tall.
     * @param footer a list of buttons to show in the footer of the screen - should include one {@link SimpleButton.Templates#done(CTxT)} / {@link SimpleButton.Close} button to close the screen
     */
    public ConfigScreen(Screen parent, @NotNull CTxT title, @NotNull CustomImage customImage, @NotNull List<SimpleButton> fileButtons, List<SimpleButton> footer) {
        super(title.b());
        this.parent = parent;
        this.customImage = customImage;
        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT+customImage.getHeight(),FOOTER_HEIGHT);
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
        this.customImage = null;
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

        this.body = this.layout.addBody(new ConfigsListWidget(client, this));
        this.body.addAllConfigEntries(this.fileButtons.stream().map(b -> ConfigsListWidget.ConfigEntry.create(b.build(this),this.body)).collect(Collectors.toList()));

        // FOOTER

        DirectionalLayoutWidget footerWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        this.footer.forEach(f -> footerWidget.add(f.build(this)));


        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    private void initHeader() {
        if (customImage != null) {
            this.layout.addHeader(new ClickableImageWidget.Builder(Text.literal("title"), this.textRenderer,
                    customImage.getImage(), customImage.getWidth(), customImage.getHeight()).build());
        } else {
            this.layout.addHeader(this.title,this.textRenderer);
        }
    }

    public ConfigsListWidget getBody() {
        return body;
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        if (this.body != null) {
            this.body.position(this.width, this.layout);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
