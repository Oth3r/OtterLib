package one.oth3r.otterlib.client.screen;

import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import one.oth3r.otterlib.Assets;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.client.screen.utl.CustomImage;
import one.oth3r.otterlib.client.screen.utl.ScreenUtl;
import one.oth3r.otterlib.client.screen.widget.ClickableImageWidget;
import one.oth3r.otterlib.client.screen.widget.TextureButtonWidget;
import one.oth3r.otterlib.file.CustomFile;

import java.net.URI;
import java.nio.file.Paths;

public class UnderConstructionScreen<T extends CustomFile<T>> extends Screen implements SetParentScreen {
    protected Screen parent;
    protected T file;
    protected TextureButtonWidget revertButton, resetButton;

    private final LinearLayout layout = LinearLayout.vertical().spacing(6);

    protected long tickTime = 0;
    protected boolean focused = true;

    public UnderConstructionScreen(Screen parent, T file) {
        super(Component.translatable("otterlib.screen.config_manager"));
        this.parent = parent;
        this.file = file;
    }

    public UnderConstructionScreen(T file) {
        super(Component.translatable("otterlib.screen.config_manager"));
        this.parent = null;
        this.file = file;
    }

    @Override
    protected void init() {
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        CTxT text = new CTxT(Component.translatable("otterlib.gui.hover.credit","@bunnestbun"));
        layout.addChild(new ClickableImageWidget.Builder(text, this.font,
                new CustomImage(Identifier.fromNamespaceAndPath(Assets.ID, "textures/gui/under_construction.png"),140,140))
                .onHover(text).onPress(ConfirmLinkScreen.confirmLink(this, URI.create("https://www.instagram.com/bunnestbun/"))).build());

        initActionButtons();
        initFooter();

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    private void initActionButtons() {
        LinearLayout actionLayout = layout.addChild(LinearLayout.horizontal().spacing(8));
        actionLayout.addChild(TextureButtonWidget.createIconButton(Component.translatable("otterlib.gui.config.button.file"),
                btn -> Util.getPlatform().openFile(this.file.getFile()),
                Identifier.fromNamespaceAndPath(Assets.ID,"icon/file")).build());

        actionLayout.addChild(TextureButtonWidget.createIconButton(
                Component.translatable("otterlib.gui.config.button.folder"),
                btn -> Util.getPlatform().openPath(Paths.get(this.file.getFile().getParent())),
                Identifier.fromNamespaceAndPath(Assets.ID,"icon/folder")).build());

        resetButton = actionLayout.addChild(TextureButtonWidget.createIconButton(
                Component.translatable("otterlib.gui.config.button.reset"),
                btn -> {
                    this.file.reset();
                    this.file.save();
                    updateButtons();
                },
                Identifier.fromNamespaceAndPath(Assets.ID, "icon/file_reset")).build());


        revertButton = actionLayout.addChild(TextureButtonWidget.createIconButton(
                Component.translatable("otterlib.gui.config.button.revert"),
                btn -> {
                    this.file.save();
                    updateButtons();
                },
                Identifier.fromNamespaceAndPath(Assets.ID, "icon/revert")).disabled(true).build());


    }

    private void initFooter() {
        LinearLayout footerLayout = layout.addChild(LinearLayout.horizontal().spacing(8));

        footerLayout.addChild(this.addRenderableWidget(new Button.Builder(Component.translatable("otterlib.gui.config.button.save_close"),
                (button) -> {
                    this.minecraft.setScreenAndShow(parent);
                }).size(140,20).build()));

        footerLayout.addChild(this.addRenderableWidget(new Button.Builder(Component.translatable("otterlib.gui.config.button.cancel"),
                (button) -> {
                    this.file.load();
                    this.file.save();
                    this.minecraft.setScreenAndShow(parent);
                }).size(140,20).build()));
    }

    @Override
    public void tick() {
        super.tick();
        tickTime++;

        // update buttons on refocus
        if (!focused && ScreenUtl.isFocused()) {
            updateButtons();
        }

        focused = ScreenUtl.isFocused();
    }

    protected void updateButtons() {
        T currentFile = this.file.clone();
        // load without saving
        currentFile.load(false);

        // revert is disabled if nothing has changed from when the player loaded the screen and the current file systsm version of the file
        revertButton.setDisabled(currentFile.equals(this.file));

        T resetFile = currentFile.clone();
        resetFile.reset();

        // reset is disabled if the current file is the same as a fresh, reset file.
        resetButton.setDisabled(currentFile.equals(resetFile));
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
        updateButtons();
    }

    @Override
    public void onClose() {
        if (parent != null) {
            this.minecraft.setScreenAndShow(parent);
        } else {
            super.onClose();
        }
    }

    @Override
    public void setParentScreen(Screen parent) {
        this.parent = parent;
    }
}
