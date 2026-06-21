package one.oth3r.otterlib.client.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.network.chat.Component;
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

    public final HeaderAndFooterLayout layout;
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
        this.layout = new HeaderAndFooterLayout(this,HEADER_HEIGHT+customBanner.getHeight(),FOOTER_HEIGHT);
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
        this.customBanner = new ClickableImageWidget.Builder(new CTxT(Component.translatable("otterlib.gui.screen.title_image")),
                this.font, customBanner).build();
        this.layout = new HeaderAndFooterLayout(this,HEADER_HEIGHT+ this.customBanner.getHeight(),FOOTER_HEIGHT);
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
        this.layout = new HeaderAndFooterLayout(this,HEADER_HEIGHT,FOOTER_HEIGHT);
        this.fileButtons = fileButtons;
        this.footer = footer;
    }

    public void setScreen(Screen screen) {
        this.minecraft.setScreenAndShow(screen);
    }

    @Override
    protected void init() {
        this.initHeader();

        this.body = this.layout.addToContents(new ConfigsListWidget(minecraft, this));
        this.body.addAllConfigEntries(this.fileButtons.stream().map(b -> ConfigsListWidget.ConfigEntry.create(b.build(this),this.body)).collect(Collectors.toList()));

        // FOOTER

        LinearLayout footerWidget = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        this.footer.forEach(f -> footerWidget.addChild(f.build(this)));


        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    private void initHeader() {
        if (customBanner != null) {
            this.layout.addToHeader(customBanner);
        } else {
            this.layout.addTitleHeader(this.title,this.font);
        }
    }

    public ConfigsListWidget getBody() {
        return body;
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        if (this.body != null) {
            this.body.updateSize(this.width, this.layout);
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreenAndShow(parent);
    }
}
