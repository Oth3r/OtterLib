package one.oth3r.fabricTest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
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
            while (keyBinding.consumeClick()) {
                client.setScreen(getConfigScreen(client.screen));
            }
        });
    }

    public static Screen getConfigScreen(Screen parent) {
        return new ConfigScreen(parent, new CTxT("test"),
                new CustomImage(Identifier.fromNamespaceAndPath(FabricTest.MOD_ID, "textures/gui/banner.png"),240, 60),
                List.of(
                        SimpleButton.Templates.fileEditor(new CTxT("Test File"),(TestFile) CustomFileReg.getFile(FabricTest.MOD_ID, TestFile.ID), new CustomImage(Identifier.fromNamespaceAndPath(FabricTest.MOD_ID, "button/server_button"),246,26)).build(),
                        SimpleButton.Templates.fileEditor(new CTxT("Test File No Image"),(TestFile) CustomFileReg.getFile(FabricTest.MOD_ID, TestFile.ID)).build(),
                        SimpleButton.Templates.wiki(new CTxT("Help")).openLink("https://oth3r.one").size(30,30).build(),
                        SimpleButton.Templates.wiki(new CTxT("Help")).openLink("https://oth3r.one").size(30,30).build(),
                        SimpleButton.Templates.warning(new CTxT("Help")).openLink("https://oth3r.one").size(150,15).hideText(false).build()
                ),
                List.of(
                        new SimpleButton.Builder(new CTxT("Donate"))
                                .miniIcon(new CustomImage(Identifier.fromNamespaceAndPath(Assets.ID, "icon/donate"),15,15)).build(),
                        SimpleButton.Templates.donate(new CTxT("Donate")).openLink(URI.create("https://ko-fi.com/oth3r")).build(),
                        SimpleButton.Templates.done(new CTxT("Done")).build(),
                        SimpleButton.Templates.wiki(new CTxT("Wiki")).openLink("https://oth3r.one").build()
                ));
    }

    private static KeyMapping keyBinding;
    private static final KeyMapping.Category TEST_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("test", "main"));

    private static void register() {
        keyBinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.fabrictest.keybind.test",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                TEST_CATEGORY
        ));

    }
}
