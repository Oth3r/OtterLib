package one.oth3r.otterlib.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import one.oth3r.otterlib.client.screen.ConfigScreen;

import java.util.List;

public class ConfigsListWidget extends ContainerObjectSelectionList<ConfigsListWidget.ConfigEntry> {
    protected final ConfigScreen configScreen;

    public ConfigsListWidget(Minecraft minecraftClient, ConfigScreen configScreen) {
        super(minecraftClient, configScreen.width, configScreen.layout.getContentHeight(), configScreen.layout.getHeaderHeight(), 32);
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

    public static class ConfigEntry extends ContainerObjectSelectionList.Entry<ConfigEntry> {
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
        public List<? extends NarratableEntry> narratables() {
            return List.of(textureButton);
        }

        /**
         * Gets a list of all child GUI elements.
         */
        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(textureButton);
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float delta) {
            int i = (this.parent.width/2) - (textureButton.getWidth()/2);

            int j = this.getContentY() - 2;
            textureButton.setPosition(i, j);
            textureButton.extractContents(graphics, mouseX, mouseY, delta);
        }
    }
}
