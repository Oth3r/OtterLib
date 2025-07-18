package one.oth3r.fabricTest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import one.oth3r.fabricTest.FabricTest;
import one.oth3r.fabricTest.TestFile;
import one.oth3r.otterlib.Assets;
import one.oth3r.otterlib.chat.CTxT;
import one.oth3r.otterlib.client.screen.ConfigScreen;
import one.oth3r.otterlib.client.screen.utl.CustomImage;
import one.oth3r.otterlib.client.screen.utl.SimpleButton;
import one.oth3r.otterlib.registry.CustomFileReg;
import org.lwjgl.glfw.GLFW;

import java.net.URI;
import java.util.List;

public class FabricTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                client.setScreen(getConfigScreen(client.currentScreen));
            }
        });
    }

    public static Screen getConfigScreen(Screen parent) {
        return new ConfigScreen(parent, new CTxT("test"),
                new CustomImage(Identifier.of(FabricTest.MOD_ID, "textures/gui/banner.png"),240, 60),
                List.of(
                        SimpleButton.Templates.fileEditor(new CTxT("Test File"),(TestFile) CustomFileReg.getFile(FabricTest.MOD_ID, TestFile.ID), new CustomImage(Identifier.of(FabricTest.MOD_ID, "button/server_button"),246,26)).build(),
                        SimpleButton.Templates.fileEditor(new CTxT("Test File No Image"),(TestFile) CustomFileReg.getFile(FabricTest.MOD_ID, TestFile.ID)).build(),
                        SimpleButton.Templates.wiki(new CTxT("Help")).openLink("https://oth3r.one").size(30,30).build(),
                        SimpleButton.Templates.wiki(new CTxT("Help")).openLink("https://oth3r.one").size(30,30).build(),
                        SimpleButton.Templates.warning(new CTxT("Help")).openLink("https://oth3r.one").size(150,15).hideText(false).build()
                ),
                List.of(
                        new SimpleButton.Builder(new CTxT("Donate"))
                                .miniIcon(new CustomImage(Identifier.of(Assets.ID, "icon/donate"),15,15)).build(),
                        SimpleButton.Templates.donate(new CTxT("Donate")).openLink(URI.create("https://ko-fi.com/oth3r")).build(),
                        SimpleButton.Templates.done(new CTxT("Done")).build(),
                        SimpleButton.Templates.wiki(new CTxT("Wiki")).openLink("https://oth3r.one").build()
                ));
    }

    private static KeyBinding keyBinding;

    private static void register() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fabrictest.keybind.test",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.fabrictest.test"
        ));

    }
}
