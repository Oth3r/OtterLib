package one.oth3r.fabricTest.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import one.oth3r.fabricTest.FabricTest;
import one.oth3r.fabricTest.client.screen.utl.FileEditorButton;
import one.oth3r.fabricTest.client.screen.utl.CustomImage;
import one.oth3r.fabricTest.client.screen.utl.SimpleButton;
import one.oth3r.fabricTest.client.screen.widget.ClickableImageWidget;
import one.oth3r.fabricTest.client.screen.widget.ConfigsListWidget;
import one.oth3r.fabricTest.client.screen.widget.TextureButtonWidget;
import one.oth3r.otterlib.file.CustomFile;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigScreen<T extends CustomFile<T>> extends Screen implements SetClientScreen{
    private static final int HEADER_HEIGHT = 30;
    protected final Screen parent;

    public final ThreePartsLayoutWidget layout;
    protected final CustomImage customImage;
    protected ConfigsListWidget body;
    protected List<FileEditorButton<T>> fileButtons;
    protected List<SimpleButton> footer;

    public ConfigScreen(Screen parent, @NotNull Text tile, @NotNull CustomImage customImage, @NotNull List<FileEditorButton<T>> fileButtons, List<SimpleButton> footer) {
        super(tile);
        this.parent = parent;
        this.customImage = customImage;
        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT+ customImage.getHeight(),40);
        this.fileButtons = fileButtons;
        this.footer = footer;
    }

//    @SuppressWarnings("unchecked")
//    public ConfigScreen(Screen parent, @NotNull Text tile, @NotNull CustomImage customImage, @NotNull List<FileEditorButton<T>> fileButtons, List<W> footerButtons) {
//        super(tile);
//        this.parent = parent;
//        this.customImage = customImage;
//        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT+ customImage.getHeight(),40);
//        this.fileButtons = fileButtons;
//        DirectionalLayoutWidget horizontalWidget = DirectionalLayoutWidget.horizontal().spacing(8);
//        footerButtons.forEach(horizontalWidget::add);
//        this.footer = (W) horizontalWidget;
//    }

    public ConfigScreen(Screen parent, @NotNull Text title) {
        super(title);
        this.parent = parent;
        this.customImage = null;
        this.layout = new ThreePartsLayoutWidget(this,HEADER_HEIGHT,40);
    }

    public void setScreen(Screen screen) {
        this.client.setScreen(screen);
    }

    @Override
    protected void init() {
        this.initHeader();

        this.body = this.layout.addBody(new ConfigsListWidget(client, this));
        this.body.addAllConfigEntries(this.fileButtons.stream().map(b -> b.build(this)).collect(Collectors.toList()));

        // FOOTER

        DirectionalLayoutWidget footerWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        this.footer.forEach(f -> footerWidget.add(f.build(this)));

//        footerWidget.add(TextureButtonWidget.createIconButton(
//                Text.translatable("sit!.gui.button.issues"),
//                ConfirmLinkScreen.opening(this, URI.create("https://github.com/Oth3r/Sit/issues")),
//                Identifier.of(FabricTest.MOD_ID, "issues")).build());
//
//        footerWidget.add(ButtonWidget.builder(Text.translatable("sit!.gui.button.website"),
//                ConfirmLinkScreen.opening(this, URI.create("https://modrinth.com/mod/sit!"))
//        ).build());
//
//        footerWidget.add(ButtonWidget.builder(Text.translatable("gui.done"), (button) -> close()).build());
//
//        footerWidget.add(TextureButtonWidget.createIconButton(
//                Text.translatable("sit!.gui.button.donate"),
//                ConfirmLinkScreen.opening(this, URI.create("https://Ko-fi.com/oth3r")),
//                Identifier.of(FabricTest.MOD_ID, "donate")).build());

        // ^^ FOOTER

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
