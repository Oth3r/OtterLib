package one.oth3r.otterlib.client.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import one.oth3r.otterlib.client.screen.ConfigScreen;

import java.util.List;

public class ConfigsListWidget extends ElementListWidget<ConfigsListWidget.ConfigEntry> {
    protected final ConfigScreen configScreen;

    public ConfigsListWidget(MinecraftClient minecraftClient, ConfigScreen configScreen) {
        super(minecraftClient, configScreen.width,
                configScreen.height,
                configScreen.layout.getHeaderHeight(),configScreen.height-configScreen.layout.getFooterHeight(), 32);
        this.configScreen = configScreen;
    }

    @Override
    public int getRowWidth() {
        return configScreen.width - 35;
    }

    public void addSingleConfigEntry(ConfigEntry option) {
        this.addEntry(option);
    }

    public void addAllConfigEntries(List<ConfigEntry> entries) {
        for (ConfigEntry entry : entries) {
            this.addEntry(entry);
        }
    }

    public static class ConfigEntry extends ElementListWidget.Entry<ConfigEntry> {
        private final TextureButtonWidget textureButton;
        private final ConfigsListWidget parent;

        ConfigEntry(TextureButtonWidget buttonWidget, ConfigsListWidget screen) {
            this.textureButton = buttonWidget;
            this.parent = screen;
        }

        public static ConfigEntry create(TextureButtonWidget buttonWidget, ConfigsListWidget screen) {

            return new ConfigEntry(buttonWidget, screen);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(textureButton);
        }

        /**
         * Gets a list of all child GUI elements.
         */
        @Override
        public List<? extends Element> children() {
            return List.of(textureButton);
        }

        /**
         * Renders an entry in a list.
         *
         * @param context
         * @param index        the index of the entry
         * @param y            the Y coordinate of the entry
         * @param x            the X coordinate of the entry
         * @param entryWidth   the width of the entry
         * @param entryHeight  the height of the entry
         * @param mouseX       the X coordinate of the mouse
         * @param mouseY       the Y coordinate of the mouse
         * @param hovered      whether the mouse is hovering over the entry
         * @param tickProgress
         */
        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
            int i = (this.parent.width/2) - (textureButton.getWidth()/2);

            int j = y - 2;
            textureButton.setPosition(i, j);
            textureButton.render(context, mouseX, mouseY, tickProgress);
        }
    }
}
