package one.oth3r.fabricTest.client.screen;

import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import one.oth3r.fabricTest.FabricTest;
import one.oth3r.fabricTest.client.screen.widget.ClickableImageWidget;
import one.oth3r.fabricTest.client.screen.widget.TextureButtonWidget;
import one.oth3r.otterlib.file.CustomFile;

import java.net.URI;
import java.nio.file.Paths;

public class UnderConstructionScreen<T extends CustomFile<T>> extends Screen {
    protected final Screen parent;
    protected T file;
    protected TextureButtonWidget revertButton, resetButton;

    private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical().spacing(6);

    protected long tickTime = 0;
    protected boolean focused = true;

    public UnderConstructionScreen(Screen parent, T file) {
        super(Text.translatable("sit!.screen.config"));
        this.parent = parent;
        this.file = file;
    }

    @Override
    protected void init() {
        this.layout.getMainPositioner().alignHorizontalCenter();

        layout.add(new ClickableImageWidget.Builder(Text.of("Art by @bunnestbun"), this.textRenderer,
                Identifier.of(FabricTest.MOD_ID, "textures/gui/fox.png"),140,140)
                .onHover(Text.of("Art by @bunnestbun")).onPress(ConfirmLinkScreen.opening(this, URI.create("https://www.instagram.com/bunnestbun/"))).build());

        initActionButtons();
        initFooter();

        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
        updateButtons();
    }

    private void initActionButtons() {
        DirectionalLayoutWidget actionLayout = layout.add(DirectionalLayoutWidget.horizontal().spacing(8));

        actionLayout.add(new ButtonWidget.Builder(Text.translatable("sit!.gui.button.file"),
                (button) -> Util.getOperatingSystem().open(this.file.getFile()))
                .dimensions(0, 0, 118 ,20).build());

        actionLayout.add(TextureButtonWidget.createIconButton(
                Text.literal("Folder"),
                btn -> Util.getOperatingSystem().open(Paths.get(this.file.getFile().getParent())),
                Identifier.of(FabricTest.MOD_ID,"folder")).build());

        resetButton = actionLayout.add(TextureButtonWidget.createIconButton(
                Text.literal("Reset"),
                btn -> {
                    this.file.reset();
                    this.file.save();
                    updateButtons();
                },
                Identifier.of(FabricTest.MOD_ID, "reset_file")).build());


        revertButton = actionLayout.add(TextureButtonWidget.createIconButton(
                Text.literal("Revert"),
                btn -> {
                    this.file.save();
                    updateButtons();
                },
                Identifier.of(FabricTest.MOD_ID, "revert_file")).disabled(true).build());


    }

    private void initFooter() {
        DirectionalLayoutWidget footerLayout = layout.add(DirectionalLayoutWidget.horizontal().spacing(8));

        footerLayout.add(this.addDrawableChild(new ButtonWidget.Builder(Text.literal("Cancel"),
                (button) -> {
                    this.client.setScreen(parent);
                }).size(140,20).build()));

        footerLayout.add(this.addDrawableChild(new ButtonWidget.Builder(Text.literal("Save & Close"),
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

        if (!focused && FabricTest.isFocused()) {
            System.out.println("check");
            updateButtons();
        }

        focused = FabricTest.isFocused();
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
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
