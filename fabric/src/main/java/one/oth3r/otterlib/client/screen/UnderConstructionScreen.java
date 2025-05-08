package one.oth3r.otterlib.client.screen;

import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import one.oth3r.otterlib.Assets;
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

    private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical().spacing(6);

    protected long tickTime = 0;
    protected boolean focused = true;

    public UnderConstructionScreen(Screen parent, T file) {
        super(Text.translatable("otterlib.screen.config_manager"));
        this.parent = parent;
        this.file = file;
    }

    public UnderConstructionScreen(T file) {
        super(Text.translatable("otterlib.screen.config_manager"));
        this.parent = null;
        this.file = file;
    }

    @Override
    protected void init() {
        this.layout.getMainPositioner().alignHorizontalCenter();
        Text text = Text.translatable("otterlib.gui.hover.credit","@bunnestbun");
        layout.add(new ClickableImageWidget.Builder(text, this.textRenderer,
                Identifier.of(Assets.ID, "textures/gui/under_construction.png"),140,140)
                .onHover(text).onPress(ConfirmLinkScreen.opening(this, URI.create("https://www.instagram.com/bunnestbun/"))).build());

        initActionButtons();
        initFooter();

        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    private void initActionButtons() {
        DirectionalLayoutWidget actionLayout = layout.add(DirectionalLayoutWidget.horizontal().spacing(8));
        actionLayout.add(TextureButtonWidget.createIconButton(Text.translatable("otterlib.gui.config.button.file"),
                btn -> Util.getOperatingSystem().open(this.file.getFile()),
                Identifier.of(Assets.ID,"icon/file")).build());

        actionLayout.add(TextureButtonWidget.createIconButton(
                Text.translatable("otterlib.gui.config.button.folder"),
                btn -> Util.getOperatingSystem().open(Paths.get(this.file.getFile().getParent())),
                Identifier.of(Assets.ID,"icon/folder")).build());

        resetButton = actionLayout.add(TextureButtonWidget.createIconButton(
                Text.translatable("otterlib.gui.config.button.reset"),
                btn -> {
                    this.file.reset();
                    this.file.save();
                    updateButtons();
                },
                Identifier.of(Assets.ID, "icon/file_reset")).build());


        revertButton = actionLayout.add(TextureButtonWidget.createIconButton(
                Text.translatable("otterlib.gui.config.button.revert"),
                btn -> {
                    this.file.save();
                    updateButtons();
                },
                Identifier.of(Assets.ID, "icon/revert")).disabled(true).build());


    }

    private void initFooter() {
        DirectionalLayoutWidget footerLayout = layout.add(DirectionalLayoutWidget.horizontal().spacing(8));

        footerLayout.add(this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("otterlib.gui.config.button.save_close"),
                (button) -> {
                    this.client.setScreen(parent);
                }).size(140,20).build()));

        footerLayout.add(this.addDrawableChild(new ButtonWidget.Builder(Text.translatable("otterlib.gui.config.button.cancel"),
                (button) -> {
                    this.file.load();
                    this.file.save();
                    this.client.setScreen(parent);
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
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        SimplePositioningWidget.setPos(this.layout, this.getNavigationFocus());
        updateButtons();
    }

    @Override
    public void close() {
        if (parent != null) {
            this.client.setScreen(parent);
        } else {
            super.close();
        }
    }

    @Override
    public void setParentScreen(Screen parent) {
        this.parent = parent;
    }
}
