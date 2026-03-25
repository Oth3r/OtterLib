package one.oth3r.otterlib.client.screen;

import net.minecraft.client.gui.screens.Screen;

/**
 * an interface for setting the parent screen of a screen after it has been initialized
 */
public interface SetParentScreen {
    void setParentScreen(Screen parent);
}
